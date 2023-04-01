package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.controller.dto.CompetitionDto;
import com.project.oag.entity.Competition;
import com.project.oag.service.CompetitionService;

@RestController
@RequestMapping("/competition")
public class CompetitionController {
	@Autowired
	CompetitionService competitionService;
	
	  @PostMapping("/create_Competition")
	    public ResponseEntity<Competition> createCompetition(@RequestBody CompetitionDto competitionDto) {
	        	Competition createdCompetition = competitionService.createCompetiton(competitionDto);
	            return ResponseEntity.ok(createdCompetition);
	  }
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Competition> deleteCompetition(@PathVariable Long id) {
	        competitionService.deleteCompetition(id);
	        return ResponseEntity.noContent().build();
	    }
	    
}
