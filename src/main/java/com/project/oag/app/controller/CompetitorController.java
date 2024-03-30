package com.project.oag.app.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.oag.app.service.CompetitionService;
import com.project.oag.app.service.CompetitorService;
import com.project.oag.app.service.VoteService;
import com.project.oag.app.model.Competition;
import com.project.oag.app.model.Competitor;
import com.project.oag.app.model.Vote;
import com.project.oag.exceptions.CompetitionNotFoundException;
import com.project.oag.exceptions.CompetitorNotFoundException;
import com.project.oag.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("api/competitors")
@CrossOrigin("http://localhost:8080/")
public class CompetitorController {
	 @Value("${uploadDir}")
	 private String uploadFolder;
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CompetitorService competitorService;

	@Autowired
	private VoteService voteService;

	public CompetitorController(CompetitorService competitorService, CompetitionService competitionService) {
		this.competitorService = competitorService;
		this.competitionService = competitionService;
	}

	@Autowired
	private CompetitionService competitionService;

	@PostMapping("/register")
	@PreAuthorize("hasRole('ARTIST')")
	public @ResponseBody ResponseEntity<?> registerCompetitor(
	        @RequestParam("firstName") String firstName,
	        @RequestParam("lastName") String lastName,
	        @RequestParam("artDescription") String artDescription,
	        @RequestParam("phone") String phone,
	        @RequestParam("email") String email,
	        @RequestParam("category") String category,
			@RequestParam("competitionId") Long competitionId,
	        Model model,
	        HttpServletRequest request,
	        final @RequestParam("image") MultipartFile file, Authentication authentication) {
	    try {
	        String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
	        log.info("uploadDirectory:: " + uploadDirectory);
	        String fileName = file.getOriginalFilename();
	        String filePath = Paths.get(uploadDirectory, fileName).toString();
	        log.info("FileName: " + file.getOriginalFilename());
	        if (fileName == null || fileName.contains("..")) {
	            model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence " + fileName);
	            return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName,
	                    HttpStatus.BAD_REQUEST);
	        }
	        String[] firstname = firstName.split(",");
	        String[] lastname = lastName.split(",");
	        String[] descriptions = artDescription.split(",");
	        Date createDate = new Date();
	        log.info("firstName: " + firstname[0] + " " + filePath);
	        log.info("lastName: " + lastname[0] + " " + filePath);
	        log.info("artDescription: " + descriptions[0]);
	        try {
	            File dir = new File(uploadDirectory);
	            if (!dir.exists()) {
	                log.info("Folder Created");
	                dir.mkdirs();
	            }
	            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
	            stream.write(file.getBytes());
	            stream.close();
	        } catch (Exception e) {
	            log.info("in catch");
	            e.printStackTrace();
	        }
	        byte[] imageData = file.getBytes();
	        Competitor competitor = new Competitor();
	        competitor.setFirstName(firstname[0]);
	        competitor.setLastName(lastname[0]);
	        competitor.setImage(imageData);
	        competitor.setEmail(email);
	        competitor.setCategory(category);
	        competitor.setArtDescription(descriptions[0]);
			Competition competition = competitionService.getCompetitionById(competitionId);
			competitor.setCompetition(competition);
	        competitorService.saveCompetitor(competitor);
	        log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
	        return new ResponseEntity<>("Competitor Saved With File - " + fileName, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        log.info("Exception: " + e);
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	 @GetMapping("/{id}/image")
	 public ResponseEntity<byte[]> getCompetitorImage(@PathVariable Long id, Model model) {
		 Competitor competitor = competitorService.getCompetitorById(id);
		 if (competitor == null) {
			 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		 }
		 byte[] imageBytes = competitor.getImage();

		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.IMAGE_PNG);
		 return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
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
		Competition competition = competitionService.getCompetitionById(competitionId);
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
