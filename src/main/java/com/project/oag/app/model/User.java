package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.oag.app.dto.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class User implements UserDetails{
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_sequence")
	@Column(name = "ID")
    private Long id;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "UUID", unique = true)
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "LOCKED")
    private Boolean locked = false;

    @Column(name = "ENABLED")
    private Boolean enabled = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @Column(name = "PHOTO")
    private String image; //avatar

	@Column(name = "SELECTED_FOR_BID")
    private boolean selectedForBid;

	@Column(name = "SECRET")
    private String secret= Base32.random();

	@Column(name = "ENABLED_2FA")
    private boolean enable2FA = false;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

	@JsonIgnoreProperties({"artwork", "user"})
    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;

	@JsonIgnore
    @OneToMany(mappedBy = "userId")
    private List<Cart> carts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "artistId")
    private List<Artwork> artworks;

    @JsonIgnore
    @OneToMany(mappedBy = "artistId")
    private List<Competitor> competitors;

    @JsonIgnore
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<PaymentLog> paymentLogs;

	@JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

	@JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Event> events;


    public void addCart(Cart cart) {
        carts.add(cart);
        cart.setUserId(this.id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}