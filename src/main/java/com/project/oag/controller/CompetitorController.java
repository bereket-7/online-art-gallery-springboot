package com.project.oag.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.FileUploadUtil;
import com.project.oag.controller.dto.CompetitorDto;
import com.project.oag.entity.Competitor;
import com.project.oag.service.CompetitorService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/competitor")
@CrossOrigin("http://localhost:8080/")
public class CompetitorController {
	
	@Autowired
	CompetitorService competitorService;
	
	private String path = "src/main/resources/static/img/competition-images/";
	public CompetitorController(CompetitorService competitorService) {
		super();
		this.competitorService = competitorService;
	}

	 @PostMapping("/upload")
	 public ResponseEntity<Competitor> competitorUpload(@ModelAttribute("competitor") Competitor competitor,@RequestParam("image") MultipartFile image) throws IOException
    {
		  String filename = StringUtils.cleanPath(image.getOriginalFilename());
			competitor.setArtworkPhoto(filename);
			competitorService.registerCompetitor(competitor);
			FileUploadUtil.uploadFile(path, filename, image);
	 
		 return new ResponseEntity<>(new Competitor(filename,"Image is successfully uploaded"),HttpStatus.OK);	
	}
	
    @GetMapping("/all")
    public List<Competitor> getAllCompetitor() {
        return competitorService.getAllCompetitors();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Competitor> getCompetitorById(@PathVariable Long id) {
        Optional<Competitor> competitor = competitorService.getCompetitorById(id);
        return competitor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
	@DeleteMapping("/delete/{id}") // delete existing competition from the database
	public void deleteCompetitor(@PathVariable Long id) { // call service method to delete existing competiton from the													// database
		competitorService.deleteCompetitor(id);
	}
	
	 @PutMapping("/update/{id}")
	    public Competitor updateCompetitor(@PathVariable Long id, @RequestBody Competitor competitor) throws Exception {
	        return competitorService.updateCompetitor(id, competitor);
	 }
	 
	    @GetMapping("/art")
	    public ResponseEntity<List<Map<String, Object>>> getAllCompetitors() {
	        List<Competitor> competitors = competitorService.getAllCompetitors();
	        List<Map<String, Object>> response = new ArrayList<>();
	        for (Competitor competitor : competitors) {
	            Map<String, Object> competitorMap = new HashMap<>();
	            competitorMap.put("artworkPhoto", competitor.getArtworkPhoto());
	            competitorMap.put("artDescription", competitor.getArtDescription());
	            competitorMap.put("category", competitor.getCategory());
	            competitorMap.put("firstName", competitor.getFirstName());
	            response.add(competitorMap);
	        }
	        return ResponseEntity.ok().body(response);
	  }

	    @PostMapping("/{id}/vote")
	    public ResponseEntity<?> vote(@PathVariable Long id, HttpServletRequest request) {
	        // Check if the user has already voted for this competitor
	        String ipAddress = request.getRemoteAddr();
	        if (competitorService.hasUserVoted(id, ipAddress)) {
	            return ResponseEntity.badRequest().body("You have already voted for this Artwork.");
	        }

	        // Increment the vote count for the competitor
	        competitorService.incrementVoteCount(id);

	        // Record the user's vote
	        competitorService.recordUserVote(id, ipAddress);

	        return ResponseEntity.ok("Thank you for voting!");
	    }
	    /*
	    @GetMapping("/top")
	    public List<Competitor> getTopCompetitors() {
	        return competitorService.getTopCompetitors();
	    }*/
	    @GetMapping("/winner")
	    public List<CompetitorDto> getTopCompetitors() {
	        List<CompetitorDto> topCompetitors = competitorService.getTopCompetitors();
			return topCompetitors;
	    } 
	    
	    
}
