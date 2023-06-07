package com.project.oag.standard;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Standard {
	@Id  //primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "standard_description", nullable = false)
	private String standardDescription;
	
	@Column(name = "standard_type", nullable = false)
	private String standardType;
	
	public Standard() {
		super();
	}
	public Standard(Long id, String standardDescription, String standardType) {
		super();
		this.id = id;
		this.standardDescription = standardDescription;
		this.standardType = standardType;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStandardDescription() {
		return standardDescription;
	}

	public void setStandardDescription(String standardDescription) {
		this.standardDescription = standardDescription;
	}

	public String getStandardType() {
		return standardType;
	}

	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}

}
