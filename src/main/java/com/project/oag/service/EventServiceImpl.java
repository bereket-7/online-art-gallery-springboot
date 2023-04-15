package com.project.oag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Event;
import com.project.oag.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {
	@Autowired
    private EventRepository eventRepository;

	@Override
	public void uploadEvent(Event event) {
		
		event.setEventName(event.getEventName());
		event.setEventDescription(event.getEventDescription());
		event.setEventphoto(event.getEventphoto());
		eventRepository.save(event);
	}
	
	/*
	 @Override
	 public EventDto registerEvent(EventDto eventDto) throws EventAlreadyRegisteredException {
	        Optional<?> existingEvent = Optional.of(eventRepository.findByEventId(eventDto.getEventName()));
	        if (existingEvent.isPresent()) {
	            throw new EventAlreadyRegisteredException("Event with ID " + eventDto.getEventName() + " already exists.");
	        }
	        Event event = new Event();
	        event.setEventName(eventDto.getEventName());
	        event.setEventDescription(eventDto.getEventDescription());
	        event.setEventPhoto(eventDto.getEventPhoto());
	        event.setTimestamp(eventDto.getTimestamp());

	        Event savedEvent = eventRepository.save(event);
	        return mapToDto(savedEvent);
	    }

	    private EventDto mapToDto(Event event) {
	        EventDto eventDto = new EventDto();
	        eventDto.setEventName(event.getEventName());
	        eventDto.setEventDescription(event.getEventDescription());
	        eventDto.setEventPhoto(event.getEventPhoto());
	        eventDto.setTimestamp(event.getTimestamp());
	        return eventDto;
	    }
	
	
    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

	@Override
	public List<?> findAll() {
	    return eventRepository.findAll();
	}

	@Override
	public Event findById(Long eventId) {
	     return eventRepository.findById(eventId)
	    		 .orElseThrow(() -> new EntityNotFoundException("Event with id " + eventId + " not found"));
	}

	@Override
	public Event update(@Valid Event event, MultipartFile eventPhoto) {
	     Event findEvent = eventRepository.findById(event.getEventId()).orElse(null);
	       if (findEvent == null) {
	           throw new EntityNotFoundException("Event with this id not found.");
	       }
	       if (eventPhoto != null && !eventPhoto.isEmpty()) {
	           findEvent.setEventPhoto(event.getEventPhoto());
	       }
	       findEvent.setEventName(event.getEventName());
	       findEvent.setEventDescription(event.getEventDescription());
	       findEvent.setTimestamp(event.getTimestamp());
	       return eventRepository.save(findEvent);
	}

	@Override
	public void deleteById(Long eventId) {
		// TODO Auto-generated method stub
		
	}

	/**@Override
    public void saveEvent(String eventName, String eventDescription, MultipartFile eventPhoto, LocalDateTime timestamp) {
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setEventPhoto(eventPhoto);
        event.setTimestamp(timestamp);
        eventRepository.save(event);
    }**/

	/**@Transactional
	public void updateEvent(Long eventId, String eventName, String eventDescription, MultipartFile eventPhoto, LocalDateTime timestamp) {
	    Optional<Event> eventOptional = eventRepository.findById(eventId);
	    if (eventOptional.isPresent()) {
	        Event eventToUpdate = eventOptional.get();
	        eventToUpdate.setEventName(eventName);
	        eventToUpdate.setEventDescription(eventDescription);
	        eventToUpdate.setEventPhoto(eventPhoto);
	        eventToUpdate.setTimestamp(timestamp);
	        eventRepository.save(eventToUpdate);
	    } else {
	    	throw new EntityNotFoundException("Event not found.");
	    }
	}**/

}
