package com.project.oag.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.oag.app.model.Cart;
import com.project.oag.app.model.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

    @Query("select c from Cart c where c.userId = ?1")
    List<Cart> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("delete from Cart c where c.userId = ?1 and c.id = ?2")
    int deleteByUserIdAndId(Long userId, Long id);

    @Transactional
    @Modifying
    @Query("delete from Cart c where c.userId = ?1")
    int deleteByUserId(Long userId);


}
