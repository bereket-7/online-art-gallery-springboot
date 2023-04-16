package com.project.oag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Long>{

    boolean findByEmail(String email);
    
}
