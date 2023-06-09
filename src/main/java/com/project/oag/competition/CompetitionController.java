package com.project.oag.competition;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/competition")
@CrossOrigin("http://localhost:8080/")
public class CompetitionController {
	@Autowired
	CompetitionService competitionService;
	@GetMapping("/all")
	public List<Competition> getAllCompetitions() {
		return competitionService.getAllCompetitions();
	}
	@GetMapping("{id}")
	public Competition getCompetitionById(@PathVariable Long id) {
		return competitionService.getCompetitionById(id);
	}
	@PostMapping("/add")
	@PreAuthorize("hasRole('MANAGER')")
	public void addCompetition(@RequestBody Competition competition) { 																// request body with a method parameter.
		competitionService.addCompetition(competition);
	}
	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public void updateCompetition(@PathVariable Long id, @RequestBody Competition competition) { 															// parameter.
		competitionService.updateCompetition(id, competition);
	}
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public void deleteCompetition(@PathVariable Long id) { 													// database
		competitionService.deleteCompetition(id);
	}
    @GetMapping("/most-recent")
	@PreAuthorize("hasRole('MANAGER','ARTIST')")
    public ResponseEntity<Competition> getMostRecentCompetition() {
		Competition competition = competitionService.getMostRecentCompetition();
		if (competition != null) {
			return ResponseEntity.ok().body(competition);
		}
		return ResponseEntity.notFound().build();
	}
    @GetMapping("/{id}/numberOfCompetitor")
	@PreAuthorize("hasRole('MANAGER','ARTIST')")
    public ResponseEntity<Integer> getNumberOfCompetitor(@PathVariable Long id) {
        Integer numberOfCompetitor = competitionService.getNumberOfCompetitor(id);
        return new ResponseEntity<>(numberOfCompetitor, HttpStatus.OK);
    }
}
