package com.project.oag.app.controller;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.project.oag.app.model.Event;
import com.project.oag.app.dto.EventDto;
import com.project.oag.app.service.EventService;
import com.project.oag.common.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("api/v1/events")
public class EventController {
	 private final EventService eventService;
	 @Value("${uploadDir}")
	 private String uploadFolder;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
	public @ResponseBody ResponseEntity<GenericResponse> createEvent(@RequestBody EventDto eventDto) {
		return eventService.createEvent(eventDto);
	}

	 @GetMapping("/{id}")
	 public ResponseEntity<GenericResponse> getEvent(@PathVariable Long id) {
	     return eventService.getEventById(id);
	}

	 @GetMapping
	 public ResponseEntity<GenericResponse> getAllEvent() {
	     return eventService.getAllEvents();
	 }
	 
	 @GetMapping("/pending")
	 @PreAuthorize("hasRole('MANAGER')")
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
		@PreAuthorize("hasRole('MANAGER')")
	    public ResponseEntity<String> acceptEvent(@PathVariable Long id) {
	        boolean accepted = eventService.acceptEvent(id);
	        if (accepted) {
	            return ResponseEntity.ok("Event with ID " + id + " has been accepted");
	        } else {
	            return ResponseEntity.badRequest().body("Event with ID " + id + " was not found or is not pending");
	        }
	    }
	    @PutMapping("/{id}/reject")
		@PreAuthorize("hasRole('MANAGER')")
	    public ResponseEntity<String> rejectEvent(@PathVariable Long id) {
			boolean rejected = eventService.rejectEvent(id);
			if (rejected) {
				return ResponseEntity.ok("Event with ID " + id + " has been Rejected due to the reason it does not satisfy company standard");
			} else {
				return ResponseEntity.badRequest().body("Event with ID " + id + " was not found or is not in pending status");
			}
		}
		@DeleteMapping("/{eventId}")
		public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
			boolean deleted = eventService.deleteEvent(eventId);
			if (deleted) {
				return new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
			}
		}
		@PutMapping("/{eventId}")
		public ResponseEntity<String> updateEvent(
				@PathVariable Long eventId,
				@RequestBody EventDto eventUpdateDTO
		) {
			String result = eventService.updateEvent(eventId, eventUpdateDTO);
			if (result.equals("Event not found")) {
				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		}

		@PostMapping("/{eventId}/image")
		 public ResponseEntity<String> changeEventImage(
		 		@PathVariable Long eventId,
		 		@RequestParam("image") MultipartFile imageFile ) throws IOException {
		 	eventService.changeEventImage(eventId, imageFile);
			return new ResponseEntity<>("Event image changed successfully", HttpStatus.OK);
		 }
}
