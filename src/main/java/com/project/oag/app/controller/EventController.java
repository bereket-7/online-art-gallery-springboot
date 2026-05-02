package com.project.oag.app.controller;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.dto.EventStatus;
import com.project.oag.app.service.EventService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

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
        return prepareResponse(HttpStatus.OK, "Event created successfully", eventService.createEvent(eventDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_EVENT')")
    public ResponseEntity<GenericResponse> getEvent(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Successfully retrieved event", eventService.getEventById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_FETCH_EVENT')")
    public ResponseEntity<GenericResponse> getAllEvent() {
        return prepareResponse(HttpStatus.OK, "Successfully retrieved all events", eventService.getAllEvents());
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_EVENT')")
    public ResponseEntity<GenericResponse> getEventsByStatus(@RequestParam(required = false) EventStatus status) {
        return prepareResponse(HttpStatus.OK, "Successfully retrieved events", eventService.getEventsByEventStatus(status));
    }

    @PatchMapping("/change/status/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_EVENT')")
    public ResponseEntity<GenericResponse> changeStatus(@PathVariable Long id, @RequestParam(required = false) EventStatus status) {
        return prepareResponse(HttpStatus.OK, "Event Status Successfully Updated", eventService.changeEventStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_EVENT')")
    public ResponseEntity<GenericResponse> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return prepareResponse(HttpStatus.OK, "Successfully deleted Event", null);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_EVENT')")
    public ResponseEntity<GenericResponse> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        return prepareResponse(HttpStatus.OK, "Saved Event ", eventService.updateEvent(id, eventDto));
    }
}