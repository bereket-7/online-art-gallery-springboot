package com.project.oag.app.controller;

import com.project.oag.app.model.Standard;
import com.project.oag.app.service.StandardService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/standards")
public class StandardController {
	private StandardService standardService;
	public StandardController(StandardService standardService) {
		this.standardService = standardService;
	}
	  @GetMapping
	public ResponseEntity<GenericResponse> getAllStandards() {
	        return standardService.getAllStandards();
	}
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN_ADD_STANDARD')")
	public ResponseEntity<GenericResponse> addStandard(@RequestBody Standard standard) {
	        return standardService.addStandard(standard);
	    }
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('MANAGER')")
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
		@PreAuthorize("hasRole('MANAGER')")
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
