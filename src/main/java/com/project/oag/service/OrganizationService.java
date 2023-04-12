package com.project.oag.service;

import java.util.List;

import com.google.common.base.Optional;
import com.project.oag.controller.dto.OrganizationDto;
import com.project.oag.entity.Organization;
import com.project.oag.exceptions.UserAlreadyExistException;

public interface OrganizationService {

	// Organization update(OrganizationDto organizationDto) throws
	// UserNotFoundException;
	void updatePassword(Organization organization, String password);

	// void updateResetPasswordToken(String token, String email) throws
	// UserNotFoundException;
	Organization getByResetPasswordToken(String token);

	Organization save(OrganizationDto organizationDto) throws UserAlreadyExistException;

	public void addOrganization(Organization org);

	Optional<Organization> getOrganizationById(Long id);

	public List<Organization> getAllOrganizations();

	public void deleteOrganization(Long id);

	public void updateOrganization(Long id, Organization org);

}
