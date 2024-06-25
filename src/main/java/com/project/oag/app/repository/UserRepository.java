package com.project.oag.app.repository;

import com.project.oag.app.dto.UserSearchResponseDto;
import com.project.oag.app.entity.User;
import io.micrometer.common.lang.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.project.oag.common.AppConstants.CACHE_KEY_EMAIL;
import static com.project.oag.common.AppConstants.CACHE_NAME_USER_INFO;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    @Query("select count(u) from User u where upper(u.userRole.roleName) = upper(?1)")
    long countUserByUserRole(String roleName);

    @Override
    void delete(User user);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);

    @Query("select u from User u " +
            "where upper(u.userRole.roleName) = upper(?1) " +
            "order by u.creationDate DESC")
    List<UserInfoByRole> findUserByRoleName(String roleName);


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

    @Query("""
            SELECT NEW com.project.oag.app.dto.UserSearchResponseDto(
            u.uuid,
            u.firstName,
            u.lastName,
            u.email,             
            u.phone,
            u.image,
            u.sex)
            FROM User u
            WHERE 
                 u.lastUpdateDate BETWEEN cast(:fromDate as timestamp) AND cast(:toDate as timestamp)
                 AND (:email IS NULL OR (upper(u.email) LIKE :email))
                 AND (:firstName IS NULL OR (upper(u.firstName) LIKE :firstName))
                 AND (:lastName IS NULL OR (upper(u.lastName) LIKE :lastName))
                 AND (:uuid IS NULL OR (upper(u.uuid) LIKE :uuid))
                 AND (:phone IS NULL OR (upper(u.phone) LIKE :phone))
                 """)
    Page<UserSearchResponseDto> findUsers(@Nullable @Param("fromDate") Timestamp fromDate,
                                                 @Nullable @Param("toDate") Timestamp toDate,
                                                 @Nullable @Param("email") String email,
                                                 @Nullable @Param("firstName") String firstName,
                                                 @Nullable @Param("lastName") String lastName,
                                                 @Nullable @Param("phone") String phone,
                                                 @Nullable @Param("uuid") String uuid,
                                                 Pageable pageable);
}
