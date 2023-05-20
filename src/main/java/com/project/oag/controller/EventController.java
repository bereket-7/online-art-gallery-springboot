package com.project.oag.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;
import com.project.oag.repository.EventRepository;
import com.project.oag.service.EventService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/event")
public class EventController {
	 @Autowired
	 private EventService eventService;
	 @Autowired
	 private EventRepository eventRepository;

	 @Value("${uploadDir}")
	 private String uploadFolder;
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	// private String path = "src/main/resources/static/img/event-images/";

	 @PostMapping("/saveEvent")
	public @ResponseBody ResponseEntity<?> createEvent(@RequestParam("eventName") String eventName,
			@RequestParam("ticketPrice") double ticketPrice,@RequestParam("capacity") int capacity, @RequestParam("eventDescription") String eventDescription,@RequestParam("location") String location,@RequestParam("eventDate") LocalDate eventDate, Model model, HttpServletRequest request
			,final @RequestParam("image") MultipartFile file) {
		try {
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
			}
			String[] names = eventName.split(",");
			String[] descriptions = eventDescription.split(",");
			Date createDate = new Date();
			log.info("eventName: " + names[0]+" "+filePath);
			log.info("eventDescription: " + descriptions[0]);
			log.info("ticketPrice: " + ticketPrice);
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
		Event event = new Event();
			event.setEventName(names[0]);
			event.setImage(imageData);
			event.setTicketPrice(ticketPrice);
			event.setCapacity(capacity);
			event.setLocation(location);
			event.setStatus("pending");
			event.setEventDate(eventDate);
			event.setEventDescription(descriptions[0]);
			event.setCreateDate(createDate);
			eventService.saveEvent(event);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Event Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	 
	 @GetMapping("/{id}")
	 public ResponseEntity<Event> getEvent(@PathVariable Long id, Model model) {
	     Optional<Event> event = eventService.getEventById(id);

	     if (event == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }

    return new ResponseEntity<>(event.get(), HttpStatus.OK);
	 }
	 
	 
	 @GetMapping("/{id}/image")
	 public ResponseEntity<byte[]> getEventImage(@PathVariable Long id, Model model) {
	     Optional<Event> event = eventService.getEventById(id);
	     if (event == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }
	     byte[] imageBytes = event.get().getImage();
	 
	     HttpHeaders headers = new HttpHeaders();
   		headers.setContentType(MediaType.IMAGE_PNG);
    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
	 }
	 
	 
	 @GetMapping
	 public ResponseEntity<List<Event>> getAllEvent() {
	     List<Event> eventList = eventService.getAllEvents();

	     if (eventList == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }

    return new ResponseEntity<>(eventList, HttpStatus.OK);
	 }
	 
	   
	   @GetMapping("/pending")
	    public List<Event> getPendingEvents() {
	        return eventService.getPendingEvents();
	    }
	    
	    @GetMapping("/accepted")
	    public List<Event> getAcceptedEvents() {
	        return eventService.getAcceptedEvents();
	    }
	    
	    @GetMapping("/rejected")
	    public List<Event> getRejectedEvents() {
	        return eventService.getRejectedEvents();
	    }
	    
	    @PutMapping("/{id}/accept")
	    public ResponseEntity<String> acceptArtwork(@PathVariable Long id) {
	        boolean accepted = eventService.acceptEvent(id);
	        if (accepted) {
	            return ResponseEntity.ok("Event with ID " + id + " has been accepted");
	        } else {
	            return ResponseEntity.badRequest().body("Event with ID " + id + " was not found or is not pending");
	        }
	    }
	    
	    @PutMapping("/{id}/reject")
	    public ResponseEntity<String> rejectArtwork(@PathVariable Long id) {
	        boolean rejected = eventService.rejectEvent(id);
	        if (rejected) {
	            return ResponseEntity.ok("Event with ID " + id + " has been Rejected due to the reason it does not satisfy company standard");
	        } else {
	            return ResponseEntity.badRequest().body("Event with ID " + id + " was not found or is not in pending status");
	        }
	    }
	    
	    
	   /*
		
		 @GetMapping("events/{id}")
		 public ResponseEntity<EventDto> getEvent(@PathVariable Long id) {
		     Optional<Event> event = eventService.getEventById(id);

		     if (event.isEmpty()) {
		         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		     }
		     byte[] imageBytes = event.get().getImage();
		     
		     EventDto eventDto = new EventDto();
		     eventDto.setImage(imageBytes);
		     eventDto.setEventName(event.get().getEventName());
		     eventDto.setEventDescription(event.get().getEventDescription());
		     eventDto.setEventDate(event.get().getEventDate());
		     eventDto.setLocation(event.get().getLocation());
		     eventDto.setCapacity(event.get().getCapacity());
		     eventDto.setTicketPrice(event.get().getTicketPrice());
		     eventDto.setStatus(event.get().getStatus()); 

		     return new ResponseEntity<>(eventDto, HttpStatus.OK);
		 }  */
		   
}
