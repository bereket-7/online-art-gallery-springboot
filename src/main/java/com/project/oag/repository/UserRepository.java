package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);

    @Override
    void delete(User user);
	/*@Query("SELECT u FROM user u WHERE u.email = ?1")
	 User findByEmail(String email);*/

}
