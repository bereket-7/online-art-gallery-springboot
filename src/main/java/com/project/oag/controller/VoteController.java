package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Competitor;
import com.project.oag.entity.User;
import com.project.oag.entity.Vote;
import com.project.oag.repository.CompetitorRepository;
import com.project.oag.repository.UserRepository;
import com.project.oag.service.CompetitorService;
import com.project.oag.service.VoteService;

@RestController
@RequestMapping("/vote")
public class VoteController {
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final CompetitorRepository competitorRepository;
	
	@Autowired
	private final VoteService voteService;
	 
	 @Autowired
	 private CompetitorService competitorService;
	 
	    public VoteController(VoteService voteService, UserRepository userRepository, CompetitorRepository competitorRepository) {
	        this.voteService = voteService;
	        this.userRepository = userRepository;
	        this.competitorRepository = competitorRepository;
	    }
	 
	 	@PostMapping
	    public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
	        Vote savedVote = voteService.save(vote);
	        return new ResponseEntity<>(savedVote, HttpStatus.CREATED);
	    }
	 	
	    @PostMapping("/vote")
	    public ResponseEntity vote(@RequestParam("userId") Long userId, @RequestParam("competitorId") Long competitorId) {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        Competitor competitor = competitorRepository.findById(competitorId)
	                .orElseThrow(() -> new RuntimeException("Artwork not found"));

	        boolean voted = voteService.vote(user, competitor);

	        if (!voted) {
	            return new ResponseEntity<>("You has already voted for this artwork", HttpStatus.BAD_REQUEST);
	        }
	        return new ResponseEntity<>("Vote successful", HttpStatus.OK);
	    }

}
