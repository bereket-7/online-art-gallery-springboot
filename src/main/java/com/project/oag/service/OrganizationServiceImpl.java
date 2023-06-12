package com.project.oag.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.oag.entity.Organization;
import com.project.oag.repository.OrganizationRepository;
@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;
	@Override
	public List<Organization> getAllOrganizations() {
		return organizationRepository.findAll();
	}
	@Override
	public Optional<Organization> getOrganizationById(Long id) {
		return organizationRepository.findById(id);
	}
	@Override
	public void addOrganization(Organization org) {
		//org.setPassword(passwordEncoder(org.getPassword()));
		organizationRepository.saveAndFlush(org);
	}
	@Override
	public void updateOrganization(Long id, Organization org) {
		organizationRepository.saveAndFlush(org);
	}
	@Override
	public void deleteOrganization(Long id) {
		organizationRepository.deleteById(id);
	}
}
