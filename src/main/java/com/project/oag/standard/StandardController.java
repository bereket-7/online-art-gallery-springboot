package com.project.oag.standard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/standards")
@CrossOrigin("http://localhost:8080/")
public class StandardController {
	  
	  @Autowired
	   private StandardService standardService;
	public StandardController(StandardService standardService) {
		this.standardService = standardService;
	}
	  
	  @GetMapping
	  public List<Standard> getAllStandards() {
	        return standardService.getAllStandards();
	    }

	    @PostMapping("/add")
	    public Standard addStandard(@RequestBody Standard standard) {
	        return standardService.addStandard(standard);
	    }

	@PutMapping("/{id}")
	public ResponseEntity<String> updateStandard(@PathVariable Long id, @RequestBody Standard updatedStandard) {
		Standard standard = standardService.getStandardById(id);
		if (standard == null) {
			return ResponseEntity.notFound().build();
		}

		standard.setStandardDescription(updatedStandard.getStandardDescription());
		standard.setStandardType(updatedStandard.getStandardType());

		standardService.addStandard(standard);

		return ResponseEntity.ok("Standard updated successfully");
	}
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteStandardById(@PathVariable Long id) {
	        standardService.deleteStandardById(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }

	@GetMapping("/search")
	public ResponseEntity<List<Standard>> searchByStandardType(@RequestParam("type") String standardType) {
		List<Standard> standards = standardService.getStandardsByType(standardType);
		if (standards.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(standards);
	}
}
