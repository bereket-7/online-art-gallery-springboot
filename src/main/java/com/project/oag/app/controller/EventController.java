package com.project.oag.app.controller;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.dto.EventStatus;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.EventService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/events")
@Tag(name = "Events")
public class EventController {
    private final EventService eventService;
    private final UserRepository userRepository;

    public EventController(EventService eventService, UserRepository userRepository) {
        this.eventService = eventService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ORG_ADD_EVENT', 'ADMIN_ADD_EVENT')")
    public ResponseEntity<GenericResponse> createEvent(HttpServletRequest request, @RequestBody EventDto eventDto) {
        return prepareResponse(HttpStatus.CREATED, "Event created successfully",
                eventService.createEvent(eventDto, resolveUser(request)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ORG_ADD_EVENT', 'ADMIN_ADD_EVENT')")
    public ResponseEntity<GenericResponse> createEventAlias(HttpServletRequest request, @RequestBody EventDto eventDto) {
        return createEvent(request, eventDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getEvent(@PathVariable Long id) {
        if (hasAuthority("ADMIN_FETCH_EVENT")) {
            return prepareResponse(HttpStatus.OK, "Successfully retrieved event", eventService.getEventById(id));
        }
        return prepareResponse(HttpStatus.OK, "Successfully retrieved event", eventService.getPublicEventById(id));
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getAllEvent(@RequestParam(required = false) EventStatus status) {
        if (hasAuthority("ADMIN_FETCH_EVENT")) {
            if (status != null) {
                return prepareResponse(HttpStatus.OK, "Successfully retrieved events",
                        eventService.getEventsByEventStatus(status));
            }
            return prepareResponse(HttpStatus.OK, "Successfully retrieved all events", eventService.getAllEvents());
        }
        return prepareResponse(HttpStatus.OK, "Successfully retrieved all events", eventService.getPublicEvents());
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
    @PreAuthorize("hasAnyAuthority('ORG_MODIFY_OWN_EVENT', 'ADMIN_MODIFY_EVENT')")
    public ResponseEntity<GenericResponse> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto,
                                                       HttpServletRequest request) {
        boolean admin = hasAuthority("ADMIN_MODIFY_EVENT");
        return prepareResponse(HttpStatus.OK, "Saved Event ",
                eventService.updateEvent(id, eventDto, resolveUser(request), admin));
    }

    private boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> authority.equals(a.getAuthority()));
    }

    private User resolveUser(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
