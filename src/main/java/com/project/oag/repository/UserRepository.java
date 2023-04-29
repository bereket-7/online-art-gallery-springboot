package com.project.oag.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Role;
import com.project.oag.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);
    
    List<User> findByRolesContains(Role role);

    @Override
    void delete(User user);
	/*@Query("SELECT u FROM user u WHERE u.email = ?1")
	 User findByEmail(String email);*/

	Optional<User> findByUsernameAndPassword(String username, String password);

}
