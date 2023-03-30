package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Cart;
import com.project.oag.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long>{

	List<Cart> findAllByUserIdOrderByCreatedDateDesc(Integer userId);
	List<Cart> deleteByUser(User user);
	List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
	void deleteById(int id);
	//Cart getOne(Integer id);

}
