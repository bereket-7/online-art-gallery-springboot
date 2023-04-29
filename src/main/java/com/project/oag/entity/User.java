package com.project.oag.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.aerogear.security.otp.api.Base32;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(nullable = true)
    private String firstname;

	@Column(nullable = true)
    private String lastname;

    @Email(message = "Email is not valid")
    private String email;

	@Column(nullable = true)
	private String phone;

    @Column(nullable = true)
	private String address;

	@Column(nullable = true)
    private String sex;

	@Column(nullable = true)
    private Integer age;

    private String username;

	@Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
    
    @Column(nullable = true)
    private String photos; 
    
	private boolean enabled;
	
	private String selectedForBid;
	
    private String secret;
    
    @Column(nullable = true)
    private String token;
    
    private boolean isUsing2FA;

	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private List<Order> orders;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bid> bid;

    public User(String firstname, String lastname, @Email(message = "Email is not valid") String email, String phone,
            String address, String sex, Integer age, String username, String password, String photos, boolean enabled,
            String selectedForBid, String secret, String token, boolean isUsing2FA, Set<Role> roles,
            List<Order> orders, List<Rating> ratings, List<Bid> bid) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.sex = sex;
        this.age = age;
        this.username = username;
        this.password = password;
        this.photos = photos;
        this.enabled = enabled;
        this.selectedForBid = selectedForBid;
        this.secret = secret;
        this.token = token;
        this.isUsing2FA = isUsing2FA;
        this.roles = roles;
        this.orders = orders;
        this.ratings = ratings;
        this.bid = bid;
    }

    public User() {
	    super();
        this.secret = Base32.random();
        this.enabled = false;
		// TODO Auto-generated constructor stub
	}
    
    
	public User(String firstname, String lastname, String phone, String address, String email, String sex,
    Integer age, String username, String password, String role) {
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

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSelectedForBid() {
        return selectedForBid;
    }

    public void setSelectedForBid(String selectedForBid) {
        this.selectedForBid = selectedForBid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
	    this.roles = new HashSet<>(roles);
	}

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Bid> getBid() {
        return bid;
    }

    public void setBid(List<Bid> bid) {
        this.bid = bid;
    }
    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;
         
        return "/user-photos/" + id + "/" + photos;
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

}
