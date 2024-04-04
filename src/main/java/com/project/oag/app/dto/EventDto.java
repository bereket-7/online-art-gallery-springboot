package com.project.oag.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Builder
@Data
public class EventDto {  
        private String eventName;
        private String eventDescription;
        private LocalDateTime eventDate;
        private String location;
		private double ticketPrice;
		private int capacity;
		private String status;    
        private Long id;
        private String imageUrl;
}
