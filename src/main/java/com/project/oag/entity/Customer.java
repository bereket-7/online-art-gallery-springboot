package com.project.oag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Customer")
public class Customer {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String firstname;
    private String lastname;
    private String email;
   private String phone;
   private String address;
   private Integer age;
   private String sex;
    private String password;
   private boolean enabled;
    private String token;


}
