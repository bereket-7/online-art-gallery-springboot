package com.project.oag.competition;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.project.oag.controller.dto.CompetitorDto;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/competitors")
@CrossOrigin("http://localhost:8080/")
public class CompetitorController {
	 @Value("${uploadDir}")
	 private String uploadFolder;
	 private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CompetitorService competitorService;
	public CompetitorController(CompetitorService competitorService) {
		super();
		this.competitorService = competitorService;
	}
	@PostMapping("/register")
	public @ResponseBody ResponseEntity<?> registerCompetitor(
	        @RequestParam("firstName") String firstName,
	        @RequestParam("lastName") String lastName,
	        @RequestParam("artDescription") String artDescription,
	        @RequestParam("phone") String phone,
	        @RequestParam("email") String email,
	        @RequestParam("category") String category,
	        Model model,
	        HttpServletRequest request,
	        final @RequestParam("image") MultipartFile file) {
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
		 Optional<Competitor> competitor = competitorService.getCompetitorById(id);
		 if (competitor == null) {
			 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		 }
		 byte[] imageBytes = competitor.get().getImage();

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
    @GetMapping("/{id}")
    public ResponseEntity<Competitor> getCompetitorById(@PathVariable Long id) {
        Optional<Competitor> competitor = competitorService.getCompetitorById(id);
        return competitor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
	@DeleteMapping("/delete/{id}")
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
	            competitorMap.put("artworkPhoto", competitor.getImage());
	            competitorMap.put("artDescription", competitor.getArtDescription());
	            competitorMap.put("category", competitor.getCategory());
	            competitorMap.put("firstName", competitor.getFirstName());
	            response.add(competitorMap);
	        }
	        return ResponseEntity.ok().body(response);
	  }
	    @PostMapping("/{id}/vote")
	    public ResponseEntity<?> vote(@PathVariable Long id, HttpServletRequest request) {
	        String ipAddress = request.getRemoteAddr();
	        if (competitorService.hasUserVoted(id, ipAddress)) {
	            return ResponseEntity.badRequest().body("You have already voted for this Artwork.");
	        }
	        competitorService.incrementVoteCount(id);
	        competitorService.recordUserVote(id, ipAddress);
	        return ResponseEntity.ok("Thank you for voting!");
	    }
	    @GetMapping("/winner")
	    public List<CompetitorDto> getTopCompetitors() {
	        List<CompetitorDto> topCompetitors = competitorService.getTopCompetitors();
			return topCompetitors;
	    }
}
