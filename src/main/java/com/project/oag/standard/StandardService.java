package com.project.oag.standard;

import java.util.List;

public interface StandardService {
	List<Standard> getAllStandards();
	Standard addStandard(Standard standard);
	Standard updateStandard(Standard newstandard);
	Standard getStandardById(Long id);
	void deleteStandardById(Long id);
}
