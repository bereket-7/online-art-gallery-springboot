package com.project.oag.app.service;

import com.project.oag.app.dto.StandardRequestDto;
import com.project.oag.app.dto.StandardType;
import com.project.oag.app.model.Standard;
import com.project.oag.app.repository.StandardRepository;
import com.project.oag.common.GenericResponse;
import com.project.oag.exceptions.GeneralException;
import com.project.oag.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.project.oag.common.AppConstants.LOG_PREFIX;
import static com.project.oag.utils.Utils.prepareResponse;

@Service
@Slf4j
public class StandardService {
    private final ModelMapper modelMapper;
    private final StandardRepository standardRepository;

    public StandardService(StandardRepository standardRepository, ModelMapper modelMapper) {
        this.standardRepository = standardRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<GenericResponse> getAllStandards() {
        try {
            List<Standard> standards = standardRepository.findAll();
            return prepareResponse(HttpStatus.OK, "Successfully fetched all Standards", standards);
        } catch (Exception e) {
            throw new GeneralException("Could not find all standards");
        }
    }

    public ResponseEntity<GenericResponse> addStandard(StandardRequestDto standardRequestDto) {
        try {
            val standard = modelMapper.map(standardRequestDto, Standard.class);
            val response = standardRepository.save(standard);
            return prepareResponse(HttpStatus.OK, "Successfully added Standard", response);
        } catch (Exception e) {
            throw new GeneralException("Failed to add Standard");
        }
    }

    public ResponseEntity<GenericResponse> deleteStandardById(final Long id) {
        try {
            standardRepository.deleteById(id);
            return prepareResponse(HttpStatus.OK, "Successfully deleted Standard", null);
        } catch (Exception e) {
            throw new GeneralException("Failed to delete");
        }
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
        } catch (Exception e) {
            throw new GeneralException("Failed to save standard information");
        }
    }

    public ResponseEntity<GenericResponse> getStandardsByType(StandardType standardType) {
        try {
            val response = standardRepository.findByStandardType(standardType);
            return prepareResponse(HttpStatus.OK, "Standards by type ", response);
        } catch (Exception e) {
            throw new GeneralException("failed to find standards by standard type " + standardType);
        }
    }

}
