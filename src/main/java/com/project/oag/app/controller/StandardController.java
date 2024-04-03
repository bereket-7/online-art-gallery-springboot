package com.project.oag.app.controller;

import com.project.oag.app.dto.StandardRequestDto;
import com.project.oag.app.dto.StandardType;
import com.project.oag.app.service.StandardService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<GenericResponse> addStandard(@RequestBody StandardRequestDto standardDto) {
	        return standardService.addStandard(standardDto);
	    }
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_MODIFY_STANDARD')")
	public ResponseEntity<GenericResponse> updateStandard(@PathVariable Long id, @RequestBody StandardRequestDto updatedStandard) {
		return standardService.updateStandard(id,updatedStandard);
	}
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_MODIFY_STANDARD')")
	public ResponseEntity<GenericResponse> deleteStandardById(@PathVariable Long id) {
	        return standardService.deleteStandardById(id);
	    }
	@GetMapping("/type")
	public ResponseEntity<GenericResponse> searchByStandardType(@RequestParam(required = false) StandardType standardType) {
		return standardService.getStandardsByType(standardType);
	}
}
