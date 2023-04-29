package com.project.oag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.oag.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	List<Event> findByStatus(String string);
	   //public List<?> findByEventId(String eventName);
}
