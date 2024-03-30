package com.project.oag.app.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.app.model.Standard;
import com.project.oag.app.repository.StandardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardService {
	public StandardService(StandardRepository standardRepository) {
		this.standardRepository = standardRepository;
	}

	@Autowired
	private StandardRepository standardRepository;


	public List<Standard> getAllStandards() {
		return standardRepository.findAll();
	}


	public Standard addStandard(Standard standard) {
		return standardRepository.save(standard);
	}


	public Standard getStandardById(Long id) {
		Optional<Standard> optionalStandard = standardRepository.findById(id);
		return optionalStandard.orElse(null);
	}

	public void deleteStandardById(final Long id) {
	        this.standardRepository.deleteById(id);;
	    }


	public Standard updateStandard(Standard newstandard) {
		Optional<Standard> optional = getOptionalByID(newstandard.getId());
		if (optional.isPresent()) {
			Standard oldstandard = optional.get();
			oldstandard.setStandardDescription(newstandard.getStandardDescription());
			oldstandard.setStandardType(newstandard.getStandardType());
			return standardRepository.save(oldstandard);
		} else {
			throw new RuntimeException("No record found for the given ID");
		}
	}
	public List<Standard> getStandardsByType(String standardType) {
		return standardRepository.findByStandardType(standardType);
	}
	private Optional<Standard> getOptionalByID(Long id) {
		return this.standardRepository.findById(id);
	}

}
