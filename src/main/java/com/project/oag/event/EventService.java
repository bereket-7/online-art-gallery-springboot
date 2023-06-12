package com.project.oag.event;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class  EventService {
	private final EventRepository eventRepository;

	public EventService(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}
	public void saveEvent(Event event) {
		eventRepository.save(event);
	}

	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}
	public Optional<Event> getEventById(Long id) {
		return eventRepository.findById(id);
	}

	public List<Event> getAllActiveEvents() {
		return eventRepository.findByStatus("active");
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
