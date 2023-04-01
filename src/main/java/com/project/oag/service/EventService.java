package com.project.oag.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.oag.controller.dto.EventDto;
import com.project.oag.entity.Event;
import com.project.oag.exceptions.EventAlreadyRegisteredException;

import jakarta.validation.Valid;

public interface EventService {

	//Event save(EventDto eventDto);

	List<?> findAll();

	Event findById(Long eventId);

	Event update(@Valid Event event, MultipartFile eventPhoto);

	void deleteEvent(Long eventId);

	//EventDto registerEvent(EventDto eventDto) throws EventAlreadyRegisteredException, EventAlreadyRegisteredException;

	void deleteById(Long eventId);

	/**@Override
	public Event save(EventDto eventDto) {
		Event event = new Event(eventDto.getEventName(),eventDto.getEventDescription(),eventDto.getEventPhoto(),eventDto.getTimestamp());
		return eventRepository.save(event);
	}**/
	EventDto registerEvent(EventDto eventDto) throws EventAlreadyRegisteredException;
	

}
