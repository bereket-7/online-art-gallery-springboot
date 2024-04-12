package com.project.oag.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_detail")
    private String reportDetail;

    @Column(name = "report_name", nullable = false)
    private String reportTitle;

    @Column(name = "reporter_name")
    private String reporterName;

    @Column(name = "reporter_email")
    private String reporterEmail;

    @Column(name = "IS_CHECKED")
    private boolean isChecked;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

}
