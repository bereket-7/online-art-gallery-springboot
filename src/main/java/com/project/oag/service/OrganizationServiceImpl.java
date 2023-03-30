package com.project.oag.service;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.oag.controller.dto.OrganizationDto;
import com.project.oag.entity.Organization;
import com.project.oag.repository.OrganizationRepository;

public class OrganizationServiceImpl implements OrganizationService{
	
	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
		super();
		this.organizationRepository = organizationRepository;
	}
	
	@Override
	public Organization save(OrganizationDto organizationDto) throws UserAlreadyRegisteredException {
		Optional<?> existingUser = Optional.of(organizationRepository.findByUsername(organizationDto.getUsername()));
		  if (existingUser.isPresent()) {
			  throw new UserAlreadyRegisteredException("Organization with username " + organizationDto.getUsername() + " already exists.");
		  }
		  else {
			  Organization organization = new Organization(organizationDto.getName(),organizationDto.getPhone(),
					  organizationDto.getAddress(),organizationDto.getEmail(),
					  organizationDto.getUsername(),passwordEncoder.encode(organizationDto.getPassword()),organizationDto.getOrganization_type());
				return organizationRepository.save(organization);
		  }	  
	}
	/* 
    @Transactional
    public Organization findByEmail(String email) {
        return organizationRepository.findByEmail(email);
    }

    @Transactional
    public Organization findByUsername(String username) {
        return organizationRepository.findByEmail(username);
    }

    public boolean userEmailExists(String email){
        return organizationRepository.findByEmail(email) != null;
    }

    public boolean userUsernameExists(String username){
    	return organizationRepository.findByUsername(username) != null;
    }
    
	@Override
	public Organization update(OrganizationDto organizationDto)throws UserNotFoundException {
		Organization findUser = organizationRepository.findByUsername(organizationDto.getUsername());;
	       if (findUser == null) {
	           throw new UserNotFoundException("User with username " + organizationDto.getUsername() + " already exists.");
	       }
	       findUser.setAddress(organizationDto.getAddress());
	       findUser.setName(organizationDto.getName());
	       findUser.setUsername(organizationDto.getUsername());
	       findUser.setPhone_No(organizationDto.getPhone());
	       return organizationRepository.save(findUser);
	}
    
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException  {
    	Organization organization = organizationRepository.findByEmail(email);
        if (organization != null) {
        	organization.setResetPasswordToken(token);
        	organizationRepository.save(organization);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }
    }*/
   
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
