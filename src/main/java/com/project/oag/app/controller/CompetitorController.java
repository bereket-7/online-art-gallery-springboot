package com.project.oag.app.controller;

import com.project.oag.app.dto.CompetitorRequestDto;
import com.project.oag.app.dto.EventDto;
import com.project.oag.app.model.Competition;
import com.project.oag.app.model.Competitor;
import com.project.oag.app.model.User;
import com.project.oag.app.model.Vote;
import com.project.oag.app.service.CompetitionService;
import com.project.oag.app.service.CompetitorService;
import com.project.oag.app.service.VoteService;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.CompetitionNotFoundException;
import com.project.oag.exceptions.CompetitorNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api/v1/competitors")
public class CompetitorController {
	 @Value("${uploadDir}")
	 private String uploadFolder;
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	 private final CompetitorService competitorService;
	private final CompetitionService competitionService;
	private final VoteService voteService;

	public CompetitorController(CompetitorService competitorService, VoteService voteService, CompetitionService competitionService) {
		this.competitorService = competitorService;
        this.voteService = voteService;
        this.competitionService = competitionService;
	}
	@PostMapping("/register")
	@PreAuthorize("hasAuthority('USER_ADD_COMPETITOR')")
	public @ResponseBody ResponseEntity<GenericResponse> createEvent(HttpServletRequest request,@RequestBody CompetitorRequestDto competitorRequestDto) {
		return competitorService.registerCompetitor(request,competitorRequestDto);
	}
	 @GetMapping
	 public ResponseEntity<List<Competitor>> getAllCompetitor() {
	     List<Competitor> competitorList = competitorService.getAllCompetitors();
	     if (competitorList == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }
     return new ResponseEntity<>(competitorList, HttpStatus.OK);
	 }
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public void deleteCompetitor(@PathVariable Long id) { // call service method to delete existing competiton from the													// database
		competitorService.deleteCompetitor(id);
	}
	 @PutMapping("/update/{id}")
	 @PreAuthorize("hasRole('MANAGER')")
	    public Competitor updateCompetitor(@PathVariable Long id, @RequestBody Competitor competitor) throws Exception {
	        return competitorService.updateCompetitor(id, competitor);
	 }
	@PostMapping("/vote")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<String> voteForCompetitor(
			@RequestParam("competitionId") Long competitionId,
			@RequestParam("competitorId") Long competitorId,
			Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		if (voteService.hasUserVotedForCompetition(user.getId(), competitionId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have already voted for this competition.");
		}
		Competitor competitor = competitorService.getCompetitorById(competitorId);
		val competition = competitionService.getCompetitionById(competitionId);
		Vote vote = new Vote();
		vote.setCompetitor(competitor);
		vote.setUser(user);
		voteService.saveVote(vote);
		competitor.incrementVoteCount();
		return ResponseEntity.ok("Thank you for voting!");
	}
	@GetMapping("/competition/{competitionId}/winner")
	@PreAuthorize("hasRole('CUSTOMER','MANAGER')")
	public ResponseEntity<?> getCompetitionWinner(@PathVariable Long competitionId) {
		Competition competition = competitionService.getCompetitionById(competitionId);
		if (competition == null) {
			return ResponseEntity.notFound().build();
		}
		Competitor winner = competition.getWinner();
		if (winner == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(winner);
	}
	@GetMapping("/competition-competitor-data")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<Map<String, Object>> getCompetitionAndCompetitorData() {
		try {
			Long competitionId = 1L;
			Long competitorId = 1L;
			Competition competition = competitionService.getCompetitionById(competitionId);
			Competitor competitor = competitorService.getCompetitorById(competitorId);
			Map<String, Object> response = new HashMap<>();
			response.put("competition", competition);
			response.put("competitor", competitor);
			return ResponseEntity.ok(response);
		} catch (CompetitionNotFoundException | CompetitorNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
