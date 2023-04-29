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

import com.project.oag.entity.Organization;
import com.project.oag.service.OrganizationService;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@GetMapping("/all")
	public List<Organization> getAllOrganizations() {
		return organizationService.getAllOrganizations();
	}

	@GetMapping("{id}")
	public Optional<Organization> getOrganizationById(@PathVariable Long id) {
		return organizationService.getOrganizationById(id);
	}

	@PostMapping("/add") // add new competition to the database
	public void addCompetition(@RequestBody Organization organization) { // RequestBody annotation is used
		organizationService.addOrganization(organization); // call service method to add new competition
	}

	@PutMapping("/update/{id}") // update existing competition in the database
	public void updateCompetition(@PathVariable Long id, @RequestBody Organization organization) { //
		organizationService.updateOrganization(id, organization);
	}

	@DeleteMapping("/delete/{id}") // delete existing competition from the database
	public void deleteCompetition(@PathVariable Long id) { // call service method to delete existi
		organizationService.deleteOrganization(id);
	}
}
