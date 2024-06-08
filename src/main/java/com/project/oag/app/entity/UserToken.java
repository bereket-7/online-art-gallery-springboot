package com.project.oag.app.entity;

import com.project.oag.app.dto.auth.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@Table(name = "USER_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID", length = 20)
    private long tokenId;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "TOKEN_TYPE", nullable = false)
    private TokenType tokenType;

    @Column(name = "IS_EXPIRED", nullable = false)
    private boolean expired;

    @Column(name = "IS_REVOKED", nullable = false)
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}