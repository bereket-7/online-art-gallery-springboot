package com.project.oag.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "ACCOUNT_LOCKOUT_RULE")
public class AccountLockoutRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_LOCKOUT_RULE_ID", length = 20)
    private long accountLockoutRuleId;
    @Column(name = "FAILURE_COUNT", unique = true, length = 3)
    private short failureCount;
    @Column(name = "BLOCK_TIME", length = 10)
    private long blockTime;
}
