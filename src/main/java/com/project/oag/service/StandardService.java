package com.project.oag.service;

import java.util.List;
import java.util.Optional;

import com.project.oag.entity.Standard;

public interface StandardService {

	List<Standard> getAllStandards();

	Standard addStandard(Standard standard);

	Optional<Standard> getStandardById(Long id);

	Standard updateStandard(Standard newstandard);

}
