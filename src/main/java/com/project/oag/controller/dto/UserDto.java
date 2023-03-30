package com.project.oag.controller.dto;

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
	 private String role;
	 private boolean emailVerified;
	 private String matchingPassword;
	 private boolean isUsing2FA;
	public UserDto() {
		super();
	}
	  public UserDto(String firstname, String lastname, String email, String phone, String address, String sex,
			Integer age, String username, String password, String role, boolean emailVerified) {
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
		this.emailVerified = emailVerified;
	}
	public boolean isEmailVerified() {
			return emailVerified;
		}

		public void setEmailVerified(boolean emailVerified) {
			this.emailVerified = emailVerified;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
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

}
