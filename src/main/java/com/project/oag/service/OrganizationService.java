 package com.project.oag.service;

import com.project.oag.controller.dto.OrganizationDto;
import com.project.oag.entity.Organization;

public interface OrganizationService {

	//Organization update(OrganizationDto organizationDto) throws UserNotFoundException;
	void updatePassword(Organization organization, String password);
	//void updateResetPasswordToken(String token, String email) throws UserNotFoundException;
	Organization getByResetPasswordToken(String token);
	Organization save(OrganizationDto organizationDto) throws UserAlreadyRegisteredException;

}
