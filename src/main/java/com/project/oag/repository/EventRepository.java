package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.oag.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long>{
	   public List<?> findByEventId(String eventName);
}
