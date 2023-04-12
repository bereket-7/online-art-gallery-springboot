package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.entity.Organization;

public interface OrganizationService {

	void updatePassword(Organization organization, String password);

	Organization getByResetPasswordToken(String token);

	public void addOrganization(Organization org);

	Optional<Organization> getOrganizationById(Long id);

	public List<Organization> getAllOrganizations();

	public void deleteOrganization(Long id);

	public void updateOrganization(Long id, Organization org);

}
