package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;

public interface EventService {

	public void saveEvent(Event event);

	public List<Event> getAllActiveImages();

	public Optional<Event> getEventById(Long id);
	/*void uploadEvent(Event event);

	boolean acceptEvent(Long id);

	boolean rejectEvent(Long id);

	List<Event> getPendingEvents();

	List<Event> getAcceptedEvents();

	List<Event> getRejectedEvents();

	Event getEvent(Long eventId);

	Resource getEventPhoto(Long eventId);

	List<EventDto> getAllEvents();

	List<EventDto> getEventsWithPhotos();

	List<Event> getAllEvent();

	List<Event> getAllEventsWithImages();
*/
	

}
