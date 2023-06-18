package com.project.oag.controller.dto;

import com.project.oag.user.Role;

public class UserDto {
	
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String sex;
    private Integer age;
    private String username;
    private String password;
	private Role role;

	public UserDto(String firstname, String lastname, String email, String phone, String address, String sex,
			Integer age, String username, String password, Role role) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.sex = sex;
		this.age = age;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
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
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("UserDto [firstName=")
				.append(firstname)
				.append(", lastname=")
				.append(lastname)
				.append(", email=")
				.append(email)
				.append(", phone=")
				.append(phone)
				.append(", address=")
				.append(address)
				.append(", sex=")
				.append(sex)
				.append(", age=")
				.append(age)
				.append(", username=")
				.append(username)
				.append(", role=")
				.append(role).append("]");
		return builder.toString();

	}
}