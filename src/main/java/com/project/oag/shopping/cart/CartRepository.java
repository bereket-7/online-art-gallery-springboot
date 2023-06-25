package com.project.oag.shopping.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.shopping.cart.Cart;
import com.project.oag.user.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    List<Cart> deleteByUser(User user);

}