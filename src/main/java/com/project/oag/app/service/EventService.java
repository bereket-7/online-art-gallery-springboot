package com.project.oag.app.service;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.dto.EventStatus;
import com.project.oag.app.entity.Event;
import com.project.oag.app.repository.EventRepository;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventService(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Event createEvent(EventDto eventDto) {
        val event = modelMapper.map(eventDto, Event.class);
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));
    }

    @Transactional
    public Event changeEventStatus(Long id, EventStatus status) {
        val event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));
        event.setStatus(status);
        return eventRepository.save(event);
    }

    public List<EventDto> getEventsByEventStatus(EventStatus status) {
        val response = eventRepository.findByStatus(status);
        return response.stream().map((element) -> modelMapper.map(element, EventDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public Event updateEvent(Long id, EventDto eventDto) {
        if (ObjectUtils.isEmpty(id))
            throw new GeneralException("Event Id needs to have a value");
        val event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));
        modelMapper.map(eventDto, event);
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(final Long id) {
        eventRepository.deleteById(id);
    }
}
