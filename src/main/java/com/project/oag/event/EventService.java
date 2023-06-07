package com.project.oag.event;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

@Service
public class EventService {
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Autowired
    private EventRepository eventRepository;
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
	public void saveEvent(Event event) {
		eventRepository.save(event);	
	}
	public List<Event> getAllActiveEvents() {
		return eventRepository.findAll();
	}

	public Optional<Event> getEventById(Long id) {
		return eventRepository.findById(id);
	}

    public List<Event> getPendingEvents() {
        return eventRepository.findByStatus("pending");
    }

	public List<Event> getAcceptedEvents() {
		 return eventRepository.findByStatus("accepted");
	}

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

    public String updateEvent(Long eventId, EventDto eventUpdateDTO) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
    
            if (eventUpdateDTO.getEventName() != null) {
                event.setEventName(eventUpdateDTO.getEventName());
            }
            if (eventUpdateDTO.getEventDescription() != null) {
                event.setEventDescription(eventUpdateDTO.getEventDescription());
            }
            if (eventUpdateDTO.getEventDate() != null) {
                event.setEventDate(eventUpdateDTO.getEventDate());
            }
            if (eventUpdateDTO.getLocation() != null) {
                event.setLocation(eventUpdateDTO.getLocation());
            }
            if (eventUpdateDTO.getCapacity() >= 0) {
                event.setCapacity(eventUpdateDTO.getCapacity());
            }
            if (eventUpdateDTO.getTicketPrice() >= 0) {
                event.setTicketPrice(eventUpdateDTO.getTicketPrice());
            }
            eventRepository.save(event);
            return "Event updated successfully";
        } else {
            return "Event not found";
        }
    }
    public void changeEventImage(Long eventId, MultipartFile imageFile) {
        try {
            Optional<Event> optionalEvent = eventRepository.findById(eventId);
            if (optionalEvent.isPresent()) {
                Event event = optionalEvent.get();
                byte[] imageBytes = imageFile.getBytes();
                event.setImage(imageBytes);
    
                eventRepository.save(event);
            } else {
                throw new IllegalArgumentException("Event not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
