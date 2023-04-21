package com.project.oag.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Competition;
import com.project.oag.service.CompetitionService;

@RestController
@RequestMapping("/competition")
public class CompetitionController {
	@Autowired
	CompetitionService competitionService;

	@GetMapping("/all")
	public List<Competition> getAllCompetitions() {
		return competitionService.getAllCompetitions();
	}

	@GetMapping("{id}")
	public Optional<Competition> getCompetitionById(@PathVariable Long id) {
		return competitionService.getCompetitionById(id);
	}

	@PostMapping("/add") // add new competition to the database
	public void addCompetition(@RequestBody Competition competition) { 																// request body with a method parameter.
		competitionService.addCompetition(competition); // call service method to add new competition to the database }
	}

	@PutMapping("/update/{id}") // update existing competition in the database
	public void updateCompetition(@PathVariable Long id, @RequestBody Competition competition) { 															// parameter.
		competitionService.updateCompetition(id, competition);
	}

	@DeleteMapping("/delete/{id}") 
	public void deleteCompetition(@PathVariable Long id) { 													// database
		competitionService.deleteCompetition(id);
	}
	
    @GetMapping("/recent")
    public ResponseEntity<List<Competition>> getRecentArtworks() {
        List<Competition> competition = (List<Competition>) competitionService.getMostRecentCompetition();
        return new ResponseEntity<>(competition, HttpStatus.OK);
    }

}
