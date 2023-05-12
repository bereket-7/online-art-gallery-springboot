package com.project.oag.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.entity.Event;
import com.project.oag.exceptions.EntityNotFoundException;
import com.project.oag.repository.EventRepository;
import com.project.oag.service.EventService;

@RestController
@RequestMapping("/event")
@CrossOrigin("http://localhost:8080/")
public class EventController {
	 @Autowired
	 private EventService eventService;
	 @Autowired
	 private EventRepository eventRepository;
	 
	 private String path = "src/main/resources/static/img/event-images/";
	   /* 
	 @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	 public ResponseEntity<Event> fileUpload(@ModelAttribute("event") Event event, @RequestParam("image") MultipartFile image) throws IOException {
	     String filename = StringUtils.cleanPath(image.getOriginalFilename());
	     try {
	         String eventPhotoPath = path + filename;
	         event.setEventPhoto(eventPhotoPath);
	         eventService.uploadEvent(event);
	         FileUploadUtil.uploadFile(path, filename, image);
	     } catch (IOException e) {
	         e.printStackTrace();
	         return new ResponseEntity<>(new Event(filename, "Image is not uploaded"), HttpStatus.OK);
	     }
	     return new ResponseEntity<>(new Event(filename, "Image is successfully uploaded"), HttpStatus.OK);
	 }
	 
	/* @GetMapping("/images")
	 public ResponseEntity<List<EventDto>> getAllEventsWithImages() {
	     List<EventDto> eventsWithImages = eventService.getAllEventsWithImages();
	     return ResponseEntity.ok(eventsWithImages);
	 }
	 
	 @GetMapping("/images")
	 public ResponseEntity<List<Event>> getAllEventsWithImages() {
	     List<Event> eventsWithImages = eventService.getAllEventsWithImages();
	     for (Event event : eventsWithImages) {
	         String imageFileName = event.getEventPhoto();
	         if (imageFileName != null) {
	             try {
	                 File imageFile = new File(path + imageFileName);
	                 if (imageFile.exists()) {
	                     byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
	                     String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	                     event.setImageBase64(base64Image);
	                 }
	             } catch (IOException e) {
	                 // Handle exception
	             }
	         }
	     }
	     return ResponseEntity.ok(eventsWithImages);
	 }
*/
	    
	    @PostMapping("/upload")
	    public Event uploadEventWithImage(@RequestParam("file") MultipartFile file,
	                                       @RequestParam("eventName") String eventName,
	                                       @RequestParam("eventDescription") String eventDescription,
	                                       @RequestParam("eventDate") LocalDate eventDate,
	                                       @RequestParam("location") String location,
	                                       @RequestParam("capacity") String capacity,
	                                       @RequestParam("ticketPrice") int ticketPrice,
	                                       @RequestParam("status") String status) throws IOException {
	        Event event = new Event();
	        event.setEventName(eventName);
	        event.setEventDescription(eventDescription);
	        event.setEventDate(eventDate);
	        event.setLocation(location);
	        event.setCapacity(capacity);
	        event.setTicketPrice(ticketPrice);
	        event.setStatus(status);
	        // Save image to database
	        event.setImage(file.getBytes());

	        return eventRepository.save(event);
	    }

	  /*  
	  @GetMapping("/{eventId}/eventPhoto")
	    public ResponseEntity getProfilePhoto(@PathVariable Long eventId) {
	        try {
	        	Event event = eventService.getEvent(eventId);
	            Resource file = eventService.getEventPhoto(eventId);
	            if (file != null) {
	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.IMAGE_JPEG);// Or use MediaType.IMAGE_PNG if the image is a PNG
	                return new ResponseEntity<>(file, headers, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	  
	  @GetMapping("/event")
	  public ResponseEntity<List<EventDto>> getAllEvents() {
	      try {
	          List<Event> events = eventService.getAllEvent();
	          List<EventDto> eventDtos = new ArrayList<>();
	          for (Event event : events) {
	              Resource file = eventService.getEventPhoto(event.getId());
	              EventDto eventDto = new EventDto(event, file);
	              eventDtos.add(eventDto);
	          }
	          return new ResponseEntity<>(eventDtos, HttpStatus.OK);
	      } catch (Exception e) {
	          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }  
	  
	  @GetMapping("/eventsWithPhotos")
	  public ResponseEntity<List<EventDto>> getEventsWithPhotos() {
	    try {
	      List<EventDto> eventDtos = eventService.getEventsWithPhotos();

	      if (!eventDtos.isEmpty()) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        return new ResponseEntity<>(eventDtos, headers, HttpStatus.OK);
	      } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	      }
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }	  

/*
@GetMapping("/event-image/{imageName}")
public ResponseEntity serveImageFile(@PathVariable String imageName) {

    try {
        Path imagePath = Paths.get(path + imageName);
        Resource resource = new UrlResource(imagePath.toUri());
        
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or any other image type
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new RuntimeException("Fail to load or read image file.");
        }
    } catch (MalformedURLException e) {
        throw new RuntimeException("Error reading the image file.");
    }
}
	 */
	 /*
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
