package com.project.oag.app.service;

import com.project.oag.app.dto.EventDto;
import com.project.oag.app.entity.Event;
import com.project.oag.app.repository.EventRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EventService eventService;

    private Event event1;
    private Event event2;

    @BeforeEach
    void setUp() {
        event1 = new Event();
        event1.setId(1L);
        event1.setEventName("Art Show 2026");

        event2 = new Event();
        event2.setId(2L);
        event2.setEventName("Local Artists Gala");
    }

    @Test
    void getAllEvents_ReturnsListOfEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));
        List<Event> results = eventService.getAllEvents();
        assertEquals(2, results.size());
        assertEquals("Art Show 2026", results.get(0).getEventName());
    }

    @Test
    void getEventById_ThrowsNotFound_WhenIdMissing() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> eventService.getEventById(99L));
    }

    @Test
    void updateEvent_SuccessfullyAppliesChanges() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));
        when(eventRepository.save(any(Event.class))).thenReturn(event1);

        EventDto updatedPayload = new EventDto();
        updatedPayload.setEventName("Art Show 2026 - Extended");
        updatedPayload.setLocation("City Hall");

        doAnswer(invocation -> {
            EventDto src = invocation.getArgument(0);
            Event dest = invocation.getArgument(1);
            dest.setEventName(src.getEventName());
            dest.setLocation(src.getLocation());
            return null;
        }).when(modelMapper).map(any(EventDto.class), any(Event.class));

        Event result = eventService.updateEvent(1L, updatedPayload);

        assertEquals("Art Show 2026 - Extended", event1.getEventName());
        assertEquals("City Hall", event1.getLocation());
        verify(eventRepository, times(1)).save(event1);
    }
}
