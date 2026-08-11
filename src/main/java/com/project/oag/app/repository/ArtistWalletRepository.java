package com.project.oag.app.repository;

import com.project.oag.app.entity.ArtistWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistWalletRepository extends JpaRepository<ArtistWallet, Long> {
    Optional<ArtistWallet> findByArtistId(Long artistId);
}
