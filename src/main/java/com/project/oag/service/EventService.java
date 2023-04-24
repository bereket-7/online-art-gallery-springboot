package com.project.oag.service;

import java.util.List;

import com.project.oag.entity.Event;

public interface EventService {

	void uploadEvent(Event event);

	List<Event> getPendingEvents();

	List<Event> getAcceptedEvents();

	List<Event> getRejectedEvent();

	boolean acceptEvent(Long id);

	boolean rejectEvent(Long id);


	

}
