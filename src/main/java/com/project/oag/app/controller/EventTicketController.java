package com.project.oag.app.controller;

import com.project.oag.app.entity.EventTicket;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.EventTicketService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.RequestUtils.getLoggedInUserName;
import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/event-tickets")
public class EventTicketController {

    private final EventTicketService eventTicketService;
    private final UserRepository userRepository;

    public EventTicketController(EventTicketService eventTicketService, UserRepository userRepository) {
        this.eventTicketService = eventTicketService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{eventId}")
    @PreAuthorize("hasAuthority('USER_ADD_ORDER')")
    public ResponseEntity<GenericResponse> purchaseTicket(@PathVariable Long eventId,
                                                          HttpServletRequest request) {
        EventTicket ticket = eventTicketService.purchaseTicket(eventId, resolveUserId(request));
        return prepareResponse(HttpStatus.CREATED, "Ticket purchased", ticket);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER_ADD_ORDER')")
    public ResponseEntity<GenericResponse> getMyTickets(HttpServletRequest request) {
        return prepareResponse(HttpStatus.OK, "Tickets retrieved", eventTicketService.getUserTickets(resolveUserId(request)));
    }

    private Long resolveUserId(HttpServletRequest request) {
        String email = getLoggedInUserName(request);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
        return user.getId();
    }
}
