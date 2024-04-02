package com.project.oag.app.model;

import com.project.oag.app.dto.StandardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "STANDARDS", indexes = {
		@Index(name = "idx_standard_standard_type", columnList = "standard_type")
})
public class Standard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "standard_description")
	private String standardDescription;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "standard_type")
	private StandardType standardType;
}
