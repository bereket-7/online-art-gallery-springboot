package com.project.oag.app.repository;

import com.project.oag.app.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<UserToken, Long> {
    @Query(" select t from UserToken t inner join User u on t.user.id = u.id " +
            "where u.id=:userId and (t.expired = false or t.revoked = false)")
    List<UserToken> findValidTokens(Long userId);

    Optional<UserToken> findByToken(String token);

    @Query("""
            select u from UserToken u
            where u.token = ?1 and u.user.id = ?2 and u.revoked = false and u.expired = false""")
    Optional<UserToken> findByTokenAndUserId(String token, long userId);
}
