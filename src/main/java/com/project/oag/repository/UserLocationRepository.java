package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.User;
import com.project.oag.entity.UserLocation;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    UserLocation findByCountryAndUser(String country, User user);

}