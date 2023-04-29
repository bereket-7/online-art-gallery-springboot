package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Standard;
import com.project.oag.repository.StandardRepository;

@Service
public class StandardServiceImpl implements StandardService {

	@Autowired
	private StandardRepository standardRepository;

	@Override
	public List<Standard> getAllStandards() {
		return standardRepository.findAll();
	}

	@Override
	public Standard addStandard(Standard standard) {
		return standardRepository.save(standard);
	}

	@Override
	public Optional<Standard> getStandardById(Long id) {
		return standardRepository.findById(id);
	}
	
	@Override
	public void deleteStandardById(final Long id) {
	        this.standardRepository.deleteById(id);;
	    }

	@Override
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

	private Optional<Standard> getOptionalByID(Long id) {
		return this.standardRepository.findById(id);
	}

}
