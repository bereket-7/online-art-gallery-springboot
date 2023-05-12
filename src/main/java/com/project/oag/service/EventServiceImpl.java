package com.project.oag.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;
import com.project.oag.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EventServiceImpl implements EventService {
	
	private String path = "src/main/resources/static/img/event-images/";
	@Autowired
    private EventRepository eventRepository;
	
	@Override
    public Event saveEventWithImage(EventDto eventDto) {
        Event event = new Event();
        event.setEventName(eventDto.getEventName());
        event.setEventDescription(eventDto.getEventDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setLocation(eventDto.getLocation());
        event.setCapacity(eventDto.getCapacity());
        event.setTicketPrice(eventDto.getTicketPrice());
        event.setStatus("pending");
        event.setImage(eventDto.getImage());
        return eventRepository.save(event);
    }
	/*
	@Override
	public void uploadEvent(Event event) {
	  event.setEventName(event.getEventName());
	  event.setEventDescription(event.getEventDescription());
	  event.setEventDate(event.getEventDate());
	  event.setLocation(event.getLocation());
	  event.setCapacity(event.getCapacity());
	  event.setStatus("pending");
	  event.setEventPhoto(event.getEventPhoto());; // Updated to setEventPhoto
	  eventRepository.save(event);
	}

	@Override
	 public List<Event> getAllEventsWithImages() {
	        List<Event> events = eventRepository.findAll();
	        for (Event event : events) {
	            String imageFileName = event.getEventPhoto();
	            if (imageFileName != null) {
	                try {
	                    File imageFile = new File(path + imageFileName);
	                    if (imageFile.exists()) {
	                        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
	                        event.setEventPhoto(imageFile.getAbsolutePath());
	                        //event.setImageBase64(Base64.getEncoder().encodeToString(imageBytes));
	                    }
	                } catch (IOException e) {
	                    // Handle exception
	                }
	            }
	        }
	        return events;
	    }
	
	@Override
	public List<Event> getAllEventsWithImages() {
	    List<Event> events = eventRepository.findAll();
	    for (Event event : events) {
	        String imageFileName = event.getEventPhoto();
	        if (imageFileName != null) {
	            try {
	                File imageFile = new File(path + imageFileName);
	                if (imageFile.exists()) {
	                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
	                    event.setEventPhoto(Base64.getEncoder().encodeToString(imageBytes));
	                }
	            } catch (IOException e) {
	                // Handle exception
	            }
	        }
	    }
	    return events;
	}
	
	/*@Override
	public List<EventDto> getAllEventsWithImages() {
	    List<Event> events = eventRepository.findAll();
	    List<EventDto> eventImageDTOs = new ArrayList<>();
	    for (Event event : events) {
	        String imageFileName = event.getEventPhoto();
	        if (imageFileName != null) {
	            try {
	                File imageFile = new File(path + imageFileName);
	                if (imageFile.exists()) {
	                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
	                    String imageData = Base64.getEncoder().encodeToString(imageBytes);
	                    EventDto eventImageDTO = new EventDto(event.getId(), event.getEventName(),
	                            event.getEventDescription(), event.getEventDate(), event.getLocation(),
	                            event.getCapacity(), event.getTicketPrice(), imageData);
	                    eventImageDTOs.add(eventImageDTO);
	                }
	            } catch (IOException e) {
	                // Handle exception
	            }
	        }
	    }
	    return eventImageDTOs;
	}

	
	 	@Override
	    public Resource getEventPhoto(Long eventId) {
	    Event event = eventRepository.findById(eventId)
	            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + eventId));
	    String eventPhotoPath = event.getEventPhoto();  // Make sure it returns the complete file path
	    if (eventPhotoPath != null) {
	        try {
	            Path filePath = Paths.get(eventPhotoPath);
	            Resource file = new UrlResource(filePath.toUri());
	            if (file.exists()) {
	                return file;
	            } else {
	                throw new EntityNotFoundException("File not found " + eventPhotoPath);
	            }
	        } catch (MalformedURLException e) {
	            throw new EntityNotFoundException("File not found " + eventPhotoPath, e);
	        }
	    }
	    return null;
	}
	 	
	  	@Override
	    public List<EventDto> getAllEvents() {
	        List<Event> events = eventRepository.findAll();
	        List<EventDto> eventDtos = new ArrayList<>();
	        for (Event event : events) {
	            String eventPhotoPath = event.getEventPhoto();
	            if (eventPhotoPath != null) {
	                try {
	                    Path filePath = Paths.get(eventPhotoPath);
	                    Resource file = new UrlResource(filePath.toUri());
	                    if (file.exists()) {
	                        EventDto eventDto = new EventDto(event, file);
	                        eventDtos.add(eventDto);
	                    }
	                } catch (MalformedURLException e) {
	                    throw new EntityNotFoundException("File not found " + eventPhotoPath, e);
	                }
	            }
	        }
	        return eventDtos;
	    }  	
	  	
	  	@Override
	    public List<EventDto> getEventsWithPhotos() {
	      List<Event> events = eventRepository.findAll();
	      List<EventDto> eventDtos = new ArrayList<>();

	      for (Event event : events) {
	        Resource file = getEventPhoto(event.getId());
	        EventDto eventDto = new EventDto(event, file);
	        eventDtos.add(eventDto);
	      }

	      return eventDtos;
	    }
	  
	  	
	  	@Override
	    public List<Event> getAllEvent() {
	        return eventRepository.findAll();
	    }
	  	
	  	
	    @Override
	    public Event getEvent(Long eventId) {
	        return eventRepository.findById(eventId)
	                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));
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
    */
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
