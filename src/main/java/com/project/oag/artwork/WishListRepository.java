package com.project.oag.artwork;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByUserIdOrderByCreatedDateDesc(Long user_id);
}