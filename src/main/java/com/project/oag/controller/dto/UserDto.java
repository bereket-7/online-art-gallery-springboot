package com.project.oag.controller.dto;

import com.project.oag.validation.PasswordMatches;
import com.project.oag.validation.ValidEmail;
import com.project.oag.validation.ValidPassword;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class UserDto {
	@NotNull
	private String firstname;

	@NotNull
	private String lastname;

	@ValidEmail
	@NotNull
	@Size(min = 1, message = "{Size.userDto.email}")
	private String email;

	@NotNull
	private String phone;
	@NotNull
	private String address;
	@NotNull
	private String sex;
	@NotNull
	private Integer age;
	@NotNull
	private String username;
	private Integer role;
	@ValidPassword
	private String password;

	@NotNull
	@Size(min = 1)
	private String matchingPassword;
	private boolean isUsing2FA;

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

	public Integer getRole() {
		return role;
	}

	public void setRole(final Integer role) {
		this.role = role;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(final String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public boolean isUsing2FA() {
		return isUsing2FA;
	}

	public void setUsing2FA(boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
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
				.append(", isUsing2FA=")
				.append(isUsing2FA)
				.append(", role=")
				.append(role).append("]");
		return builder.toString();

	}
}