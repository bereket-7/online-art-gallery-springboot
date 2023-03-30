package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.controller.dto.UserDto;
import com.project.oag.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByUserEmailIgnoreCase(String emailId);
	User save(UserDto userDto);
	User findByUsername(String username);
	User findByResetPasswordToken(String token);
	User findByEmail(String email);
	/*@Query("SELECT u FROM user u WHERE u.email = ?1")
	 User findByEmail(String email);*/
}
