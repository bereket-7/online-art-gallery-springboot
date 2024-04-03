package com.project.oag.app.model;

import com.project.oag.app.dto.StandardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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

	@CreationTimestamp
	@Column(name = "CREATION_DATE")
	private Timestamp creationDate;

	@UpdateTimestamp
	@Column(name = "LAST_UPDATE_DATE")
	private Timestamp lastUpdateDate;
}
