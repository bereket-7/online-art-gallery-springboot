package com.project.oag.shopping.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.oag.exceptions.UserNotFoundException;
import com.project.oag.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.shopping.cart.Cart;
import com.project.oag.user.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    List<Cart> deleteByUser(User user);

        @Query(value = "SELECT c.id, c.quantity, a.artwork_name " +
                "FROM cart c " +
                "JOIN artwork a ON c.artwork_id = a.id " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE u.email = :email",
                nativeQuery = true)
        List<Object[]> getCartsWithEmailAndArtworkName(@Param("email") String email);

}
