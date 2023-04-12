package com.project.oag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.entity.Organization;
import com.project.oag.service.OrganizationService;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

	@Autowired
	private OrganizationService organizationService;

	@GetMapping("/organization/all")
	public List<Organization> getAllOrganizations() {
		return organizationService.getAllOrganizations();
	}

	@GetMapping("{id}")
	public Optional<Competition> getCompetitionById(@PathVariable Long id) {
		return organizationService.getOrganizationById(id);
	}

	@PostMapping("/competition/add") // add new competition to the database
	public void addCompetition(@RequestBody Competition competition) { // RequestBody annotation is used to bind the
																		// request body with a method parameter.
		competitionService.addCompetition(competition); // call service method to add new competition to the database }
	}

	@PutMapping("/update/{id}") // update existing competition in the database
	public void updateCompetition(@PathVariable Long id, @RequestBody Competition competition) { // RequestBody
																									// annotation is
																									// used to bind the
																									// request body with
																									// a method
																									// parameter.
		competitionService.updateCompetition(id, competition); // call service method to update existing competition in
																// the database }
	}

	@DeleteMapping("/delete/{id}") // delete existing competition from the database
	public void deleteCompetition(@PathVariable Long id) { // call service method to delete existing competiton from the
															// database
		competitionService.deleteCompetition(id);
	}
	/**
	 * @Autowired
	 *            private OrganizationService organizationService;
	 * 
	 *            public OrganizationController(OrganizationService
	 *            organizationService) {
	 *            super();
	 *            this.organizationService = organizationService;
	 *            }
	 *            @ModelAttribute("organization")
	 *            public OrganizationDto organizationDto() {
	 *            return new OrganizationDto();
	 *            }
	 * @GetMapping
	 *             public String showRegistrationForm() {
	 *             return "registration";
	 *             }
	 * 
	 * @PostMapping
	 *              public String
	 *              registerOrganization(@ModelAttribute("organization")
	 *              OrganizationDto organizationDto) throws
	 *              UserAlreadyRegisteredException {
	 *              organizationService.save(organizationDto);
	 *              return "redirect:/registration?success";
	 *              }
	 **/
}
