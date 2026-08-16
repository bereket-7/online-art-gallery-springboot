package com.project.oag.app.controller;

import com.project.oag.app.dto.CompetitionDto;
import com.project.oag.app.service.CompetitionService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/competitions")
@Tag(name = "Competitions")
public class CompetitionCatalogController {
    private final CompetitionService competitionService;

    public CompetitionCatalogController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getAllCompetitions() {
        return prepareResponse(HttpStatus.OK, "Competitions ", competitionService.getAllCompetitions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse> getCompetitionById(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Successfully retrieved competition",
                competitionService.getCompetitionById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_ADD_COMPETITION')")
    public ResponseEntity<GenericResponse> addCompetition(@RequestBody CompetitionDto competition) {
        return prepareResponse(HttpStatus.CREATED, "Successfully added competition",
                competitionService.addCompetition(competition));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_MODIFY_COMPETITION')")
    public ResponseEntity<GenericResponse> updateCompetition(@PathVariable Long id,
                                                             @RequestBody CompetitionDto competition) {
        return prepareResponse(HttpStatus.OK, "Successfully updated competition",
                competitionService.updateCompetition(id, competition));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_COMPETITION')")
    public ResponseEntity<GenericResponse> deleteCompetition(@PathVariable Long id) {
        competitionService.deleteCompetition(id);
        return prepareResponse(HttpStatus.OK, "Successfully deleted competition", null);
    }
}
