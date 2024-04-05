package com.project.oag.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.project.oag.app.model.Event;
import com.project.oag.app.repository.EventRepository;
import com.project.oag.app.dto.EventDto;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class  EventService {
	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	public EventService(EventRepository eventRepository, ModelMapper modelMapper) {
		this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }
	public ResponseEntity<GenericResponse> createEvent(EventDto eventDto) {
		try {
			val user = modelMapper.map(eventDto, Event.class);
			val response = eventRepository.save(user);
			return prepareResponse(HttpStatus.OK,"Event created successfully",response);
		} catch (Exception e) {
			throw new GeneralException(" Failed to save event");
		}
	}
	public ResponseEntity<GenericResponse> getAllEvents() {
		eventRepository.findAll();
	}
	public Optional<Event> getEventById(Long id) {
		return eventRepository.findById(id);
	}
	public boolean acceptEvent(Long id) {
		Optional<Event> optionalEvent = eventRepository.findById(id);
		if (optionalEvent.isPresent()) {
			Event event = optionalEvent.get();
			if (event.getStatus().equals("pending")) {
				event.setStatus("accepted");
				eventRepository.save(event);
				return true;
			}
		}
		return false;
	}
	public boolean rejectEvent(Long id) {
		Optional<Event> optionalEvent = eventRepository.findById(id);
		if (optionalEvent.isPresent()) {
			Event event = optionalEvent.get();
			if (event.getStatus().equals("pending")) {
				event.setStatus("rejected");
				eventRepository.save(event);
				return true;
			}
		}
		return false;
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
	public boolean deleteEvent(Long eventId) {
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isPresent()) {
			eventRepository.deleteById(eventId);
			return true;
		}
		return false;
	}

	public String updateEvent(Long eventId, EventDto eventUpdateDTO) {
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isPresent()) {
			Event event = optionalEvent.get();
			event.setEventName(eventUpdateDTO.getEventName());
			event.setEventDescription(eventUpdateDTO.getEventDescription());
			event.setTicketPrice(eventUpdateDTO.getTicketPrice());
			event.setCapacity(eventUpdateDTO.getCapacity());
			event.setLocation(eventUpdateDTO.getLocation());
			event.setEventDate(eventUpdateDTO.getEventDate());
			eventRepository.save(event);
			return "Event updated successfully";
		}
		return "Event not found";
	}

	public void changeEventImage(Long eventId, MultipartFile imageFile) throws IOException {
		Optional<Event> optionalEvent = eventRepository.findById(eventId);
		if (optionalEvent.isPresent()) {
			Event event = optionalEvent.get();
			byte[] imageData = imageFile.getBytes();
			event.setImage(imageData);
			eventRepository.save(event);
		}
	}

}
