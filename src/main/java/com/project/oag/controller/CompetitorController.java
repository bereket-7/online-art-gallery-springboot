package com.project.oag.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.entity.Competitor;
import com.project.oag.repository.CompetitorRepository;
import com.project.oag.service.CompetitorService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/competitor")
public class CompetitorController {
	
	@Value("${uploadDir}")
	private String uploadFolder;
	
	@Autowired
	CompetitorService competitorService;
	
	@Autowired
	CompetitorRepository competitorRepository;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public CompetitorController(CompetitorService competitorService) {
		super();
		this.competitorService = competitorService;
	}

	@PostMapping("/register_competitor")
	public @ResponseBody ResponseEntity<?> registerCompetitor(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName,@RequestParam("email") String email,
			@RequestParam("phone") String phone,@RequestParam("artDescription") String artDescription, Model model, HttpServletRequest request
			,final @RequestParam("artwork") MultipartFile art) {
		try {
			//String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = art.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + art.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
			}
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(art.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = art.getBytes();
			Competitor competitor = new Competitor();
			//competitor.setArtworkName(artwork_names[0]);
			competitor.setFirstName(firstName);
			competitor.setLastName(lastName);
			competitor.setEmail(email);
			competitor.setPhone(phone);
			competitor.setArtwork(imageData);
			competitorService.registerCompetitor(competitor);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Artwork Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
	
    @GetMapping
    public List<Competitor> getAllCompetitor() {
        return competitorService.getAllCompetitors();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Competitor> getCompetitorById(@PathVariable Long id) {
        Optional<Competitor> competitor = competitorService.getCompetitorById(id);
        return competitor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    

}
