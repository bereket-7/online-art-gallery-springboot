package com.project.oag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;
import com.project.oag.service.EventAlreadyRegisteredException;
import com.project.oag.service.EventService;

import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {
	/** @Autowired
	private EventService eventService;
	
	 public EventController(EventService eventService) {
		 super();
		 this.eventService = eventService;
	}
	 
	@ModelAttribute("event")
	public EventDto eventDto() {
	     return new EventDto();
	   }
	@GetMapping
	public String showEventForm() {
		return "event";
	}
	

    @PostMapping
    public ResponseEntity<EventDto> registerEvent(@RequestBody EventDto eventDto) {
        try {
            EventDto savedEventDto = eventService.registerEvent(eventDto);
            return ResponseEntity.ok(savedEventDto);
        } catch (EventAlreadyRegisteredException e) {
            return ResponseEntity.badRequest().build();
        }
    }
	 
	@PutMapping("/{eventId}")
	   public ResponseEntity<Event> update(@PathVariable Long eventId, @Valid Event event,
	      @RequestParam(name = "photo", required = false) MultipartFile photo) throws IOException {
	       event.setId(eventId);
	       Event updatedEvent = eventService.update(event, photo);
	       return ResponseEntity.ok(updatedEvent);
	   }**/
	
    /**@PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long eventId, @RequestBody @Valid EventDto eventDto) {
        EventDto updatedEvent = eventService.updateEvent(eventId, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }**/
	/**
	   @DeleteMapping("/{eventId}")
	   public ResponseEntity<?> deleteById(@PathVariable Long eventId) {
	       eventService.deleteById(eventId);
	       return ResponseEntity.noContent().build();
	   }

	   @GetMapping
	   public ResponseEntity<?> findAll() {
	       List<?> events = eventService.findAll();
	       return ResponseEntity.ok(events);
	   }

	   @GetMapping("/{eventId}")
	   public ResponseEntity<Event> findById(@PathVariable Long eventId) {
	       Event event = eventService.findById(eventId);
	       if (event == null) {
	           return ResponseEntity.notFound().build();
	       }
	       return ResponseEntity.ok(event);
	   }
	   
	    @GetMapping("/event/{eventId}/view")
	    public String viewEvent(@PathVariable Long eventId, Model model) {
	        Event event = eventService.findById(eventId);
	        model.addAttribute("event", event);
	        return "event-view";
	    }*/
}
