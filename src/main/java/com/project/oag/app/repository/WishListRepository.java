package com.project.oag.app.repository;

import com.project.oag.app.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    @Transactional
    @Modifying
    @Query("delete from WishList w where w.id = ?1 and w.userId = ?2")
    int deleteByIdAndUserId(Long id, Long userId);

    @Query("select w from WishList w where w.userId = ?1")
    List<WishList> findByUserId(Long userId);

}