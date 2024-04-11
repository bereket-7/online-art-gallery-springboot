package com.project.oag.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.app.model.Cart;
import com.project.oag.app.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

    List<Cart> deleteByUser(User user);

        @Query(value = "SELECT c.id, c.quantity, a.artwork_name " +
                "FROM cart c " +
                "JOIN artwork a ON c.artwork_id = a.id " +
                "JOIN users u ON c.user_id = u.id " +
                "WHERE u.email = :email",
                nativeQuery = true)
        List<Object[]> getCartsWithEmailAndArtworkName(@Param("email") String email);

    @Query("select c from Cart c where c.userId = ?1")
    List<Cart> findByUserId(Long userId);


}
