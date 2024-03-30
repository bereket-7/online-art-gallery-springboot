package com.project.oag.app.repository;

import java.util.List;

import com.project.oag.app.model.WishList;
import com.project.oag.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByUserIdOrderByCreatedDateDesc(Long user_id);
    List<WishList> findByUser(User user);
}