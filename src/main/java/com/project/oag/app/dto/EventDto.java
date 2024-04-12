package com.project.oag.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private String eventName;

    private String eventDescription;

    private String location;

    private double ticketPrice;

    private int capacity;

    private EventStatus status;

    private String imageUrl;

    private LocalDateTime eventDate;
}
