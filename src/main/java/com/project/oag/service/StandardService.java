package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.controller.dto.StandardDto;
import com.project.oag.entity.Standard;
import com.project.oag.repository.StandardRepository;

@Service
public class StandardService {
	@Autowired
	private StandardRepository standardRepository;
    public List<Standard> getAll() {
        return standardRepository.findAll();
    }
    public Optional<Standard> getById(Long id) {
        return standardRepository.findById(id);
    }
    public Standard saveStandard(StandardDto standardDto) {
    	Standard standard = new Standard(standardDto.getStandardDescription(),standardDto.getStandardType());
    	return standardRepository.save(standard);
    	
    }
	public Standard updateStandard(Long id, StandardDto standardDto) {
		Standard newStandard = new Standard();
		newStandard.setStandardDescription(standardDto.getStandardDescription());
		newStandard.setStandardType(standardDto.getStandardType());
		return standardRepository.save(newStandard);
		
	}
	public void deleteStandard(Long id) {
		standardRepository.deleteById(id);		
	}
}
