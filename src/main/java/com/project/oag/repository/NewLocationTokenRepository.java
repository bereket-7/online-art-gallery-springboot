package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.NewLocationToken;
import com.project.oag.entity.UserLocation;

public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, Long> {

    NewLocationToken findByToken(String token);

    NewLocationToken findByUserLocation(UserLocation userLocation);

}