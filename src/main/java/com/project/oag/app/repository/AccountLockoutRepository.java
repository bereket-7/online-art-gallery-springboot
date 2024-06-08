package com.project.oag.app.repository;

import com.project.oag.app.entity.AccountLockoutRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountLockoutRepository extends JpaRepository<AccountLockoutRule, Long> {
}
