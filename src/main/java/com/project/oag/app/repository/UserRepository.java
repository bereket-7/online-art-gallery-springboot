package com.project.oag.app.repository;

import com.project.oag.app.dto.Role;
import com.project.oag.app.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.project.oag.common.AppConstants.CACHE_KEY_EMAIL;
import static com.project.oag.common.AppConstants.CACHE_NAME_USER_INFO;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<User> findByUsernameContainingIgnoreCase(String username);

    Long countTotalUsersByRole(Role role);

    @Override
    void delete(User user);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    List<User> findByRole(Role role);
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where upper(u.email) = upper(?2)")
    @CacheEvict(value = CACHE_NAME_USER_INFO, key = CACHE_KEY_EMAIL)
    int updatePasswordByEmailIgnoreCase(String password, String email);
    @Transactional
    @Modifying
    @Query("update User u set u.verified = ?1 where upper(u.email) = upper(?2)")
    @CacheEvict(value = CACHE_NAME_USER_INFO, key = CACHE_KEY_EMAIL)
    int updateVerifiedByEmailIgnoreCase(boolean verified, String email);
    @Query("select (count(u) > 0) from User u where upper(u.email) = upper(?1) and u.userRole.admin = ?2")
    boolean existsByUsernameAndIsAdmin(String email, boolean admin);
}
