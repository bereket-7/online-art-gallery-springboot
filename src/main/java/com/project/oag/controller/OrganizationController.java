package com.project.oag.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organization")
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
