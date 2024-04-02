package com.project.oag.app.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.app.model.Standard;
import com.project.oag.app.repository.StandardRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class StandardService {
	private StandardRepository standardRepository;
	public StandardService(StandardRepository standardRepository) {
		this.standardRepository = standardRepository;
	}

	public ResponseEntity<GenericResponse> getAllStandards() {
		try {
			List<Standard> standards = standardRepository.findAll();
			return prepareResponse(HttpStatus.OK, "Successfully fetched all Standards",standards);
		} catch (Exception e) {
			throw new GeneralException("Could not find all standards");
		}
	}


	public ResponseEntity<GenericResponse> addStandard(Standard standard) {
		try {
			standardRepository.save(standard);
			return prepareResponse(HttpStatus.OK, "Successfully added Standard",standard);
		} catch (Exception e) {
			throw new GeneralException("Failed to add Standard");
		}
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
