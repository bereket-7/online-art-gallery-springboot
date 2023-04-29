package com.project.oag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Role {	
	  	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

		@Enumerated(EnumType.STRING)
		@Column(length = 20)
		private RoleType name;

	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public RoleType getName() {
			return name;
		}

		public void setName(RoleType name) {
			this.name = name;
		}

		public Role() {
	        super();
	    }

		public Role(String name) {
			// TODO Auto-generated constructor stub
		}
		
		
		public enum RoleType {
		    ADMIN,
		    CUSTOMER,
		    ARTIST,
		    ORGANIZATION,
		    MANAGER
		    
		}

}
