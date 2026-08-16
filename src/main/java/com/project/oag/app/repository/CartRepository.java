package com.project.oag.app.repository;

import com.project.oag.app.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.user.id = ?1")
    List<Cart> findByUserId(Long userId);

    Optional<Cart> findByUserIdAndArtworkId(Long userId, Long artworkId);

    Optional<Cart> findByIdAndUserId(Long id, Long userId);

    @Transactional
    @Modifying
    @Query("delete from Cart c where c.user.id = ?1 and c.id = ?2")
    int deleteByUserIdAndId(Long userId, Long id);

    @Transactional
    @Modifying
    @Query("delete from Cart c where c.user.id = ?1")
    int deleteByUserId(Long userId);

    @Query("SELECT SUM(c.quantity * a.price) FROM Cart c INNER JOIN c.artwork a WHERE c.user.id = :userId")
    BigDecimal calculateTotalPriceByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("delete from Cart c where c.user.id = :userId and c.artwork.id in :artworkIds")
    int deleteByUserIdAndArtworkIdIn(@Param("userId") Long userId, @Param("artworkIds") List<Long> artworkIds);
}
