package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.oag.app.dto.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENT")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Column(name = "EVENT_NAME")
    private String eventName;

    @Column(name = "EVENT_DESCRIPTION")
    private String eventDescription;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "CAPACITY")
    private int capacity;

    @Column(name = "TICKET_PRICE")
    private double ticketPrice;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "EVENT_DATE")
    private LocalDate eventDate;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnoreProperties({"artworks", "notifications", "carts", "ratings"})
    private User user;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;
}