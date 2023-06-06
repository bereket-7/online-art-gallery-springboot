package com.project.oag.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;
import com.project.oag.repository.EventRepository;
import jakarta.transaction.Transactional;

@Service
public class EventServiceImpl implements EventService {
	
	private String path = "src/main/resources/static/img/event-images/";
	@Autowired
    private EventRepository eventRepository;
	
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
	
	@Override
	public void saveEvent(Event event) {
		eventRepository.save(event);	
	}

	@Override
	public List<Event> getAllActiveEvents() {
		return eventRepository.findAll();
	}
	
	@Override
	public Optional<Event> getEventById(Long id) {
		return eventRepository.findById(id);
	}
	
    @Override
    public List<Event> getPendingEvents() {
        return eventRepository.findByStatus("pending");
    }
    
    @Override
	public List<Event> getAcceptedEvents() {
		 return eventRepository.findByStatus("accepted");
	}

    @Override
	public List<Event> getRejectedEvents() {
		 return eventRepository.findByStatus("rejected");
	}
    
    @Transactional
    public boolean acceptEvent(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
        	 Event event = eventOptional.get();
            if (event.getStatus().equals("pending")) {
            	event.setStatus("accepted");
                eventRepository.save(event);
                return true;
            }
        }
        return false;
    }
    @Transactional
	public boolean rejectEvent(Long id) {
    	 Optional<Event> eventOptional = eventRepository.findById(id);
         if (eventOptional.isPresent()) {
             Event event = eventOptional.get();
             if (event.getStatus().equals("pending")) {
                 event.setStatus("rejected");
                 eventRepository.save(event);
                 return true;
             }
         }
         return false;
	}

    
    public boolean deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        } else {
            return false;
        }
    }

}
