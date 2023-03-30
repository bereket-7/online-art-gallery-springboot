package com.project.oag.entity;

import java.util.Collection;
import java.util.List;

import org.jboss.aerogear.security.otp.api.Base32;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
	@Column(name = "first_name", nullable = false, length=100)
    private String firstname;

    @NotBlank(message = "Last name is required")
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

    @NotBlank(message = "Sex is required")
	@Column(name = "sex", nullable = false)
    private String sex;

    @NotNull(message = "Age is required")
	@Column(name = "age", length=4)
    private Integer age;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
	@Column(name = "password",nullable = false)
    private String password;
    
    /*@NotBlank(message = "role is required")
	@Column(name = "role",nullable = false)
    private String role;*/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
    
	@Column(name = "reset_password_token", length=30)
    private String resetPasswordToken;
	
	@Column(name = "verificationCode", updatable=false)
	private String verificationCode;

	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private List<Order> orders;
	
	private boolean emailVerified;
	

    private boolean isUsing2FA;

    private String secret;
    
	public User() {
		super();
		this.secret = Base32.random();
		this.emailVerified = false;
		// TODO Auto-generated constructor stub
	}


	public User(@NotBlank(message = "First name is required") String firstname,
			@NotBlank(message = "Last name is required") String lastname,
			@NotBlank(message = "Email is required") @Email(message = "Email is not valid") String email,
			@NotBlank(message = "Phone number is required") String phone, String address,
			@NotBlank(message = "Sex is required") String sex, @NotNull(message = "Age is required") Integer age,
			@NotBlank(message = "Username is required") String username,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
			Collection<Role> roles, String resetPasswordToken, String verificationCode, List<Order> orders,
			boolean emailVerified) {
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
		this.roles = roles;
		this.resetPasswordToken = resetPasswordToken;
		this.verificationCode = verificationCode;
		this.orders = orders;
		this.emailVerified = emailVerified;
	}

	public User(String firstname, String lastname, String phone, String address, String email, String sex,
			Integer age, String username, String password, String role) {
	}

	public boolean isEmailVerified() {
			return emailVerified;
		}

		public void setEmailVerified(boolean emailVerified) {
			this.emailVerified = emailVerified;
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

	public Collection<Role> getRoles() {
		return roles;
	}


	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
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
	
	public boolean isUsing2FA() {
		return isUsing2FA;
	}


	public void setUsing2FA(boolean isUsing2FA) {
		this.isUsing2FA = isUsing2FA;
	}


	public String getSecret() {
		return secret;
	}


	public void setSecret(String secret) {
		this.secret = secret;
	}


	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((getEmail() == null) ? 0 : getEmail().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        if (!getEmail().equals(user.getEmail())) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", phone=" + phone + ", address=" + address + ", sex=" + sex + ", age=" + age + ", username="
				+ username + ", password=" + password + ", roles=" + roles + ", resetPasswordToken="
				+ resetPasswordToken + ", verificationCode=" + verificationCode + ", orders=" + orders
				+ ", emailVerified=" + emailVerified + ", isUsing2FA=" + isUsing2FA + ", secret=" + secret + "]";
	}

}
