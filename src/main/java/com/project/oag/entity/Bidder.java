package com.project.oag.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Bidder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @NotBlank(message = "First name is required")
	@Column(name = "first_name", nullable = false, length=100)
    private String firstname;

	@Column(name = "last_name", nullable = false, length=100)
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Phone number is required")
	@Column(name = "phone", nullable = false, length=15)
	private String phone;

    @Column(name = "address", nullable = false)
	private String address;
    
    @OneToMany(mappedBy = "bidder",fetch = FetchType.LAZY)
	private List<Bid> bid;

	public Bidder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Bidder(@NotBlank(message = "First name is required") String firstname, String lastname,
			@NotBlank(message = "Email is required") @Email(message = "Email is not valid") String email,
			@NotBlank(message = "Phone number is required") String phone, String address, List<Bid> bid) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.bid = bid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Bid> getBid() {
		return bid;
	}

	public void setBid(List<Bid> bid) {
		this.bid = bid;
	}

	@Override
	public String toString() {
		return "Bidder [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", phone=" + phone + ", address=" + address + ", bid=" + bid + "]";
	}
	
}
