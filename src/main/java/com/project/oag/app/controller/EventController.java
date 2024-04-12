package com.project.oag.app.controller;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.dto.EventStatus;
import com.project.oag.app.service.EventService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN_ADD_EVENT')")
    public @ResponseBody ResponseEntity<GenericResponse> createEvent(@RequestBody EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_EVENT')")
    public ResponseEntity<GenericResponse> getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_FETCH_EVENT')")
    public ResponseEntity<GenericResponse> getAllEvent() {
        return eventService.getAllEvents();
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_EVENT')")
    public ResponseEntity<GenericResponse> getEventsByStatus(@RequestParam(required = false) EventStatus status) {
        return eventService.getEventsByEventStatus(status);
    }

    @PatchMapping("/change/status/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_EVENT')")
    public ResponseEntity<GenericResponse> changeStatus(@PathVariable Long id, @RequestParam(required = false) EventStatus status) {
        return eventService.changeEventStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_EVENT')")
    public ResponseEntity<GenericResponse> deleteEvent(@PathVariable Long id) {
        return eventService.deleteEvent(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_EVENT')")
    public ResponseEntity<GenericResponse> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        return eventService.updateEvent(id, eventDto);
    }
}