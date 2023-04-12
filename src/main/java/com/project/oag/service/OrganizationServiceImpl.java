package com.project.oag.service;

import java.util.List;
import java.util.Optional;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.oag.entity.Organization;
import com.project.oag.repository.OrganizationRepository;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
		super();
		this.organizationRepository = organizationRepository;
	}

	/*
	 * @Override
	 * public Organization save(OrganizationDto organizationDto) throws
	 * UserAlreadyExistException {
	 * Optional<?> existingUser =
	 * Optional.of(organizationRepository.findByUsername(organizationDto.getUsername
	 * ()));
	 * if (existingUser.isPresent()) {
	 * throw new UserAlreadyExistException(
	 * "Organization with username " + organizationDto.getUsername() +
	 * " already exists.");
	 * } else {
	 * Organization organization = new Organization(organizationDto.getName(),
	 * organizationDto.getPhone(),
	 * organizationDto.getAddress(), organizationDto.getEmail(),
	 * organizationDto.getUsername(),
	 * passwordEncoder.encode(organizationDto.getPassword()),
	 * organizationDto.getOrganization_type());
	 * return organizationRepository.save(organization);
	 * }
	 * }
	 */

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
	/*
	 * @Transactional
	 * public Organization findByEmail(String email) {
	 * return organizationRepository.findByEmail(email);
	 * }
	 * 
	 * @Transactional
	 * public Organization findByUsername(String username) {
	 * return organizationRepository.findByEmail(username);
	 * }
	 * 
	 * public boolean userEmailExists(String email){
	 * return organizationRepository.findByEmail(email) != null;
	 * }
	 * 
	 * public boolean userUsernameExists(String username){
	 * return organizationRepository.findByUsername(username) != null;
	 * }
	 * 
	 * @Override
	 * public Organization update(OrganizationDto organizationDto)throws
	 * UserNotFoundException {
	 * Organization findUser =
	 * organizationRepository.findByUsername(organizationDto.getUsername());;
	 * if (findUser == null) {
	 * throw new UserNotFoundException("User with username " +
	 * organizationDto.getUsername() + " already exists.");
	 * }
	 * findUser.setAddress(organizationDto.getAddress());
	 * findUser.setName(organizationDto.getName());
	 * findUser.setUsername(organizationDto.getUsername());
	 * findUser.setPhone_No(organizationDto.getPhone());
	 * return organizationRepository.save(findUser);
	 * }
	 * 
	 * public void updateResetPasswordToken(String token, String email) throws
	 * UserNotFoundException {
	 * Organization organization = organizationRepository.findByEmail(email);
	 * if (organization != null) {
	 * organization.setResetPasswordToken(token);
	 * organizationRepository.save(organization);
	 * } else {
	 * throw new UserNotFoundException("Could not find any user with the email " +
	 * email);
	 * }
	 * }
	 */

	public Organization getByResetPasswordToken(String token) {
		return organizationRepository.findByResetPasswordToken(token);
	}

	public void updatePassword(Organization organization, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		organization.setPassword(encodedPassword);

		organization.setResetPasswordToken(null);
		organizationRepository.save(organization);
	}

}
