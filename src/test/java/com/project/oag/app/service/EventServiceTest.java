package com.project.oag.app.service;

import com.project.oag.app.entity.Event;
import com.project.oag.app.repository.EventRepository;
import com.project.oag.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event1;
    private Event event2;

    @BeforeEach
    void setUp() {
        event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Art Show 2026");
        
        event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Local Artists Gala");
    }

    @Test
    void getAllEvents_ReturnsListOfEvents() {
        // Arrange
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // Act
        List<Event> results = eventService.getAllEvents();

        // Assert
        assertEquals(2, results.size());
        assertEquals("Art Show 2026", results.get(0).getTitle());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void getEventById_ThrowsNotFound_WhenIdMissing() {
        // Arrange
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getEventById(99L);
        });
    }

    @Test
    void updateEvent_SuccessfullyAppliesChanges() {
        // Arrange
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));
        when(eventRepository.save(any(Event.class))).thenReturn(event1);

        Event updatedPayload = new Event();
        updatedPayload.setTitle("Art Show 2026 - Extended");
        updatedPayload.setAddress("City Hall");

        // Act
        Event result = eventService.updateEvent(1L, updatedPayload);

        // Assert
        assertEquals("Art Show 2026 - Extended", event1.getTitle());
        assertEquals("City Hall", event1.getAddress());
        verify(eventRepository, times(1)).save(event1);
    }
}
