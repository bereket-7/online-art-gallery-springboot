package com.project.oag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.oag.controller.dto.OrganizationDto;
import com.project.oag.service.OrganizationService;
import com.project.oag.service.UserAlreadyRegisteredException;

@RestController
@RequestMapping("/api/organization")
public class OrganizationController {
	/**@Autowired
	private OrganizationService organizationService;

	   public OrganizationController(OrganizationService organizationService) {
		super();
		this.organizationService = organizationService;
	    }
		@ModelAttribute("organization")
	    public OrganizationDto organizationDto() {
	        return new OrganizationDto();
	    }
		@GetMapping
		public String showRegistrationForm() {
			return "registration";
		}
		
		@PostMapping
		public String registerOrganization(@ModelAttribute("organization") OrganizationDto organizationDto) throws UserAlreadyRegisteredException {
			organizationService.save(organizationDto);
			return "redirect:/registration?success";
		}**/
}
