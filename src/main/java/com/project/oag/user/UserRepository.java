package com.project.oag.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.oag.user.Role;
import com.project.oag.user.User;
@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long>{
    //User findByEmail(String email);
    Long countTotalUsersByRole(Role role);
    List<User> findByRoleContains(Role role);

    @Override
    void delete(User user);    

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);
    
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);

}
