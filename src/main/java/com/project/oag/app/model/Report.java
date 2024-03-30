package com.project.oag.app.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
