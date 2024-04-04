package com.project.oag.app.repository;

import java.util.List;
import java.util.Optional;

import com.project.oag.app.dto.Role;
import com.project.oag.app.model.User;
import jakarta.persistence.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmailIgnoreCase(String email);


    @Query(value = "SELECT u.id, u.firstname, u.lastname, u.email, a.id AS artwork_id, a.artworkName, a.artworkDescription, a.image, a.price " +
            "FROM users u " +
            "JOIN Artwork a ON u.id = a.artist_id " +
            "WHERE u.role = 'ARTIST'",
            nativeQuery = true)
    List<Object[]> findArtistsWithArtworks();
    List<User> findByUsernameContainingIgnoreCase(String username);
    Long countTotalUsersByRole(Role role);
    @Override
    void delete(User user);
    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    User findByUsername(String username);
}
