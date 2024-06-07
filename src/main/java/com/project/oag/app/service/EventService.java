package com.project.oag.app.service;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.dto.EventStatus;
import com.project.oag.app.entity.Event;
import com.project.oag.app.repository.EventRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class EventService {
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
            return prepareResponse(HttpStatus.OK, "Event created successfully", response);
        } catch (Exception e) {
            throw new GeneralException(" Failed to save event");
        }
    }

    public ResponseEntity<GenericResponse> getAllEvents() {
        try {
            val response = eventRepository.findAll();
            return prepareResponse(HttpStatus.OK, "Successfully retrieved all events", response);
        } catch (Exception e) {
            throw new GeneralException("Failed to get events");
        }
    }

    public ResponseEntity<GenericResponse> getEventById(Long id) {
        try {
            val response = eventRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));
            return prepareResponse(HttpStatus.OK, "Successfully retrieved event", response);
        } catch (Exception e) {
            throw new GeneralException("Failed to get event ");
        }
    }

    public ResponseEntity<GenericResponse> changeEventStatus(Long id, EventStatus status) {
        try {
            val event = eventRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));
            event.setStatus(status);
            eventRepository.save(event);
            return prepareResponse(HttpStatus.OK, "Event Status Successfully Updated", event);
        } catch (Exception e) {
            throw new GeneralException("failed to update event status");
        }
    }

    public ResponseEntity<GenericResponse> getEventsByEventStatus(EventStatus status) {
        try {
            val response = eventRepository.findByStatus(status);
            List<EventDto> events = response.stream().map((element) -> modelMapper.map(element, EventDto.class))
                    .collect(Collectors.toList());
            return prepareResponse(HttpStatus.OK, "Successfully retrieved events", events);
        } catch (Exception e) {
            throw new GeneralException(" failed to get events by status ");
        }
    }

    public ResponseEntity<GenericResponse> updateEvent(Long id, EventDto eventDto) {
        if (ObjectUtils.isEmpty(id))
            throw new GeneralException("Event Id needs to have a value");
        val event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));
        try {
            modelMapper.map(eventDto, event);
            val response = eventRepository.save(event);
            log.info(LOG_PREFIX, "Saved Event information", "");
            return prepareResponse(HttpStatus.OK, "Saved Event ", response);
        } catch (Exception e) {
            throw new GeneralException("Failed to save Event information");
        }
    }

    public ResponseEntity<GenericResponse> deleteEvent(final Long id) {
        try {
            eventRepository.deleteById(id);
            return prepareResponse(HttpStatus.OK, "Successfully deleted Event", null);
        } catch (Exception e) {
            throw new GeneralException("Failed to delete ");
        }
    }

}
