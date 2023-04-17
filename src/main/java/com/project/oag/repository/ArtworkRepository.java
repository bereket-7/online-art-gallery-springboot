package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Artwork;

@Repository
public interface ArtworkRepository  extends JpaRepository<Artwork, Long>{

	
}
