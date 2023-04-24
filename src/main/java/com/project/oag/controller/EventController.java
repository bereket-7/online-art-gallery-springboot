package com.project.oag.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.FileUploadUtil;
import com.project.oag.entity.Artwork;
import com.project.oag.entity.Event;
import com.project.oag.service.EventService;

@RestController
@RequestMapping("/event")
@CrossOrigin("http://localhost:8080/")
public class EventController {
	 @Autowired
	 private EventService eventService;
	 
	 private String path = "src/main/resources/static/img/event-images/";
	
	 @PostMapping("/upload")
	 public ResponseEntity<Event> fileUpload(@ModelAttribute("event") Event event,@RequestParam("image") MultipartFile image) throws IOException
     {
		  String filename = StringUtils.cleanPath(image.getOriginalFilename());
		try {
			event.setEventphoto(filename);
			eventService.uploadEvent(event);
			FileUploadUtil.uploadFile(path, filename, image);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(new Event(filename,"Image is not uploaded"),HttpStatus.OK);
		}	 
		 return new ResponseEntity<>(new Event(filename,"Image is successfully uploaded"),HttpStatus.OK);	
	}
	 
	 
	 @GetMapping("/pending")
	    public List<Event> getPendingArtworks() {
	        return eventService.getPendingEvents();
	    }
	    
	    @GetMapping("/accepted")
	    public List<Event> getAcceptedArtworks() {
	        return eventService.getAcceptedEvents();
	    }
	    
	    @GetMapping("/rejected")
	    public List<Event> getRejectedArtworks() {
	        return eventService.getRejectedEvent();
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
		public String uploadEvent(String path,MultipartFile file) throws IOException{
			
			 String filename = file.getOriginalFilename();
			 String filePath = path + File.separator + filename;
			 
			 File f = new File(path);
			 
			 if(!f.exists()) {
				 f.mkdir();
			 }
			 
	         //Path filePath = uploadPath.resolve(fileName);
	         //Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			 
			Files.copy(file.getInputStream(), Paths.get(filePath));
			
			//return eventRepository.save();
			 
			 return filename;
			
		}*/
}
