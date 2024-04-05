package com.project.oag.app.controller;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.dto.EventStatus;
import com.project.oag.app.service.EventService;
import com.project.oag.common.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
	 public ResponseEntity<GenericResponse> getPendingEventsByStatus(@RequestParam(required = false) EventStatus status) {
		return eventService.getEventsByEventStatus(status);
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
		@DeleteMapping("/{id}")
		public ResponseEntity<GenericResponse> deleteEvent(@PathVariable Long id) {
			return eventService.deleteEvent(id);
		}
		@PutMapping("/{eventId}")
		public ResponseEntity<GenericResponse> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
			return eventService.updateEvent(id,eventDto);
		}

		@PostMapping("/{eventId}/image")
		 public ResponseEntity<String> changeEventImage(
		 		@PathVariable Long eventId,
		 		@RequestParam("image") MultipartFile imageFile ) throws IOException {
		 	eventService.changeEventImage(eventId, imageFile);
			return new ResponseEntity<>("Event image changed successfully", HttpStatus.OK);
		 }
}
