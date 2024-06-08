package com.project.oag.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.oag.app.dto.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Setter
@Getter
@Table(name = "USERS", indexes = {
        @Index(name = "u_email_index", columnList = "EMAIL"),
        @Index(name = "u_phone_index", columnList = "PHONE"),
        @Index(name = "u_first_name_index", columnList = "FIRST_NAME"),
        @Index(name = "u_uuid_index", columnList = "UUID"),
        @Index(name = "u_deleted_index", columnList = "IS_DELETED"),
        @Index(name = "u_verified_index", columnList = "ACCOUNT_VERIFIED"),
})
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE USERS SET is_deleted = true WHERE USER_ID = ?")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", length = 20)
    private long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

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
    private String secret = Base32.random();

    @Column(name = "ACCOUNT_VERIFIED", nullable = false)
    private boolean verified = false;
    @Column(name = "IS_DELETED", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;
    @Column(name = "ENABLED_2FA")
    private boolean enable2FA = false;

    @Column(name = "RESET_PASSWORD_TOKEN")
    private String resetPasswordToken;
    @Column(name = "LOGIN_ATTEMPTED_NUMBER", nullable = false, length = 3)
    private int loginAttemptedNumber = 0;
    @Column(name = "BLOCKED_UNTIL")
    private Timestamp blockedUntil;
    @Column(name = "TOKEN_CREATION_TIME")
    private Timestamp tokenCreationTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID")
    @JsonManagedReference
    private UserRole userRole;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UserToken> userToken;

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
    @OneToMany(mappedBy = "user")
    private List<Cart> carts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "artist")
    private List<Artwork> artworks;

    @JsonIgnore
    @OneToMany(mappedBy = "artist")
    private List<Competitor> competitors;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PaymentLog> paymentLogs;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Event> events;


    public void addCart(Cart cart) {
        carts.add(cart);
        //cart.setUserId(this.id);
    }
}