package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "CHECKED")
    private boolean checked;

    @Column(name = "TARGET_EMAIL")
    private String targetEmail;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

    public void markAsRead() {
        this.checked = true;
    }

}

