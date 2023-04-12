package com.project.oag.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Standard;
import com.project.oag.service.StandardService;

@RestController
@RequestMapping("/standards")
public class StandardController {
	
	  @Autowired
	   private StandardService standardService;
	  
	  @GetMapping("/all")
	    public List<Standard> getAllStandards() {
	        return standardService.getAllStandards();
	    }

	    @PostMapping("/add")
	    public Standard addStandard(@RequestBody Standard standard) {
	        return standardService.addStandard(standard);
	    }

	    @PutMapping("/update") 
	    public Standard updateStandard(@RequestBody Standard standard) { 
	        return standardService.updateStandard(standard); 
	    } 

	    @DeleteMapping("/delete/{id}") 
	    public void deleteStandard(@PathVariable Long id) { 
	        standardService.deleteStandard(id); 
	    } 

	     @GetMapping("/{id}") 
	     public Optional<Standard> getStandardById(@PathVariable Long id) { 
	         return standardService.getStandardById(id); 
	     }    
}
