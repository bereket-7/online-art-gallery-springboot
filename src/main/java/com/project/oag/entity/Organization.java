package com.project.oag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "organization")
public class Organization {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
    @NotBlank(message = "name is required")
	@Column(name = "orginazrion_name", nullable = false, length=100)
    private String Name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String Email;

    @NotBlank(message = "Phone number is required")
	@Column(name = "phone", nullable = false, length=15)
	private String Phone_No;

    @Column(name = "address", nullable = false)
	private String Address;
    
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
	@Column(name = "password",nullable = false)
    private String password;
    
	@Column(name = "organization_type",nullable = false)
    private String organization_type;

	@Column(name = "reset_password_token", length=30)
    private String resetPasswordToken;
	
	@Column(name = "verificationCode", updatable=false)
	private String verificationCode;
	

	public Organization() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Organization(@NotBlank(message = "name is required") String name,
			@NotBlank(message = "Email is required") String email,
			@NotBlank(message = "Phone number is required") String phone_No, String address,
			@NotBlank(message = "Username is required") String username,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
			String organization_type, String resetPasswordToken, String verificationCode) {
		super();
		Name = name;
		Email = email;
		Phone_No = phone_No;
		Address = address;
		this.username = username;
		this.password = password;
		this.organization_type = organization_type;
		this.resetPasswordToken = resetPasswordToken;
		this.verificationCode = verificationCode;
	}


	public Organization(String name, String phone, String address, String email, String username, String password,
			String organization_type) {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone_No() {
		return Phone_No;
	}

	public void setPhone_No(String phone_No) {
		Phone_No = phone_No;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrganization_type() {
		return organization_type;
	}

	public void setOrganization_type(String organization_type) {
		this.organization_type = organization_type;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", Name=" + Name + ", Email=" + Email + ", Phone_No=" + Phone_No
				+ ", Address=" + Address + ", username=" + username + ", password=" + password + ", organization_type="
				+ organization_type + ", resetPasswordToken=" + resetPasswordToken + ", verificationCode="
				+ verificationCode + "]";
	}	   
}
