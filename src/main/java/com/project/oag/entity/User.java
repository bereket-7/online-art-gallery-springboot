package com.project.oag.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.aerogear.security.otp.api.Base32;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails{
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
    
    @Column(nullable = true)
    private LocalDateTime expirationTime;

	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private List<Order> orders;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rating> ratings;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Bid> bid;

    public User(String firstname, 
    		String lastname, 
    	    String email, String phone,
            String address, 
            String sex, Integer age,
            String username, String password, 
            String photos, boolean enabled,
            String selectedForBid, 
            String secret, String token, 
            boolean isUsing2FA, 
            Set<Role> roles,
            List<Order> orders, 
            List<Rating> ratings, 
            List<Bid> bid) {
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
   
    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;
         
        return "/user-photos/" + id + "/" + photos;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 SimpleGrantedAuthority authority =
	                new SimpleGrantedAuthority(appUserRole.name());
	        return Collections.singletonList(authority);
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = (prime * result) + ((getEmail() == null) ? 0 : getEmail().hashCode());
//        return result;
//    }
//
//    @Override
//    public boolean equals(final Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final User user = (User) obj;
//        if (!getEmail().equals(user.getEmail())) {
//            return false;
//        }
//        return true;
//    }

}
