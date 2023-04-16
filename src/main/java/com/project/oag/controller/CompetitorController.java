package com.project.oag.controller;

import java.io.IOException;
import java.util.List;
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
import com.project.oag.entity.Competition;
import com.project.oag.entity.Competitor;
import com.project.oag.entity.Event;
import com.project.oag.repository.CompetitorRepository;
import com.project.oag.service.CompetitorService;

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
	 public ResponseEntity<Event> competitorUpload(@ModelAttribute("competitor") Competitor competitor,@RequestParam("image") MultipartFile image) throws IOException
    {
		 //String uploadDir = "images/" + savedUser.getId();
		  String filename = StringUtils.cleanPath(image.getOriginalFilename());
		 //String filename = null;
		//try {
			competitor.setArtworkPhoto(filename);
			competitorService.registerCompetitor(competitor);
			FileUploadUtil.uploadFile(path, filename, image);
		//} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//return new ResponseEntity<>(new Event(filename,"Image is not uploaded"),HttpStatus.OK);
		//}	 
		 return new ResponseEntity<>(new Event(filename,"Image is successfully uploaded"),HttpStatus.OK);	
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
}
