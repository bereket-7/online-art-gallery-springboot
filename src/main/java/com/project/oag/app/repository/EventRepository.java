package com.project.oag.app.repository;

import java.util.List;

import com.project.oag.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{
	List<Event> findByStatus(String string);
}
