package com.project.oag.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.controller.dto.StandardDto;
import com.project.oag.entity.Standard;
import com.project.oag.service.StandardService;

@RestController
@RequestMapping("/api/standard")
public class StandardController {
	
	  @Autowired
	   private StandardService standardService;

	   @GetMapping("/standards")
	   public List<Standard> getAllStandards() {
	       return standardService.getAll();
	   }
	  

	   @GetMapping("/{id}")
	   public ResponseEntity<Standard> getById(@PathVariable("id") Long id) {
	       Optional<Standard> standard = standardService.getById(id);
	       if (standard.isPresent()) {
	           return new ResponseEntity<>(standard.get(), HttpStatus.OK);
	       } else {
	           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	       }
	   }
	   
	   @GetMapping("/{standardType}")
	    public ResponseEntity<Standard> getStandardByType(@PathVariable Long id) {
	        Optional<Standard> standard = standardService.getById(id);
	        return standard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	   @PostMapping("/add_standard")
	   public ResponseEntity<Standard> saveStandard(@RequestBody StandardDto standardDto) {
	       Standard standard = standardService.saveStandard(standardDto);
	       return ResponseEntity.ok(standard);
	   }
	   @PutMapping("/{id}")
	   public ResponseEntity<Standard> update(@PathVariable Long id, @RequestBody StandardDto standardDto) {
	       Standard existingStandard = standardService.updateStandard(id,standardDto);
	          return ResponseEntity.ok(existingStandard);
	   }

	  @DeleteMapping("/{id}")
	  public ResponseEntity<Standard> deleteStandard(@PathVariable Long id) {
		  
		   standardService.deleteStandard(id);
	        return ResponseEntity.noContent().build();
	  }
	    
}
