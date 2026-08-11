package com.project.oag.app.service;

import com.project.oag.app.entity.Event;
import com.project.oag.app.entity.EventTicket;
import com.project.oag.app.entity.User;
import com.project.oag.app.repository.EventRepository;
import com.project.oag.app.repository.EventTicketRepository;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventTicketService {

    private final EventTicketRepository eventTicketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventTicketService(EventTicketRepository eventTicketRepository,
                            EventRepository eventRepository,
                            UserRepository userRepository) {
        this.eventTicketRepository = eventTicketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventTicket purchaseTicket(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long sold = eventTicketRepository.countByEventId(eventId);
        if (sold >= event.getCapacity()) {
            throw new GeneralException("Event is sold out");
        }

        EventTicket ticket = new EventTicket();
        ticket.setEvent(event);
        ticket.setUser(user);
        return eventTicketRepository.save(ticket);
    }

    public List<EventTicket> getUserTickets(Long userId) {
        return eventTicketRepository.findByUserId(userId);
    }
}
