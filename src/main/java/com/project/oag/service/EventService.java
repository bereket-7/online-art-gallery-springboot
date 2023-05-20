package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;

public interface EventService {

	public void saveEvent(Event event);
	public List<Event> getAllEvents();

	public List<Event> getAllActiveEvents();

	boolean acceptEvent(Long id);

	boolean rejectEvent(Long id);

	List<Event> getPendingEvents();

	List<Event> getAcceptedEvents();

	List<Event> getRejectedEvents();

	void deleteEvent(Long eventId);
	
	Optional<Event> getEventById(Long id);
	

}
