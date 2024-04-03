package com.project.oag.app.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.app.dto.StandardRequestDto;
import com.project.oag.app.model.Standard;
import com.project.oag.app.repository.StandardRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class StandardService {
	private StandardRepository standardRepository;
	private final ModelMapper modelMapper;
	public StandardService(StandardRepository standardRepository, ModelMapper modelMapper) {
		this.standardRepository = standardRepository;
        this.modelMapper = modelMapper;
    }

	public ResponseEntity<GenericResponse> getAllStandards() {
		try {
			List<Standard> standards = standardRepository.findAll();
			return prepareResponse(HttpStatus.OK, "Successfully fetched all Standards",standards);
		} catch (Exception e) {
			throw new GeneralException("Could not find all standards");
		}
	}
	public ResponseEntity<GenericResponse> addStandard(StandardRequestDto standardRequestDto) {
		try {
			val standard = modelMapper.map(standardRequestDto, Standard.class);
			val response = standardRepository.save(standard);
			return prepareResponse(HttpStatus.OK, "Successfully added Standard",response);
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


	public ResponseEntity<GenericResponse> updateStandard(Long id, StandardRequestDto updatedStandard) {
		if (ObjectUtils.isEmpty(id))
			throw new GeneralException("Standard Id needs to have a value");
		val standard = standardRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Standard record not found"));
		try {
			modelMapper.map(updatedStandard, standard);
			val response = standardRepository.save(standard);
			log.info(LOG_PREFIX, "Saved standard information", "");
			return prepareResponse(HttpStatus.OK, "Saved standard ", response);
		}catch (Exception e){
			throw new GeneralException("Failed to save standard information");
		}
	}
	public List<Standard> getStandardsByType(String standardType) {
		return standardRepository.findByStandardType(standardType);
	}
	private Optional<Standard> getOptionalByID(Long id) {
		return this.standardRepository.findById(id);
	}

}
