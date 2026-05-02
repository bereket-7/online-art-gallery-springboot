package com.project.oag.app.controller;

import com.project.oag.app.dto.CompetitorRequestDto;
import com.project.oag.app.service.CompetitionService;
import com.project.oag.app.service.CompetitorService;
import com.project.oag.app.service.VoteService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/competitors")
public class CompetitorController {
    private final CompetitorService competitorService;
    private final CompetitionService competitionService;
    private final VoteService voteService;

    public CompetitorController(CompetitorService competitorService, VoteService voteService, CompetitionService competitionService) {
        this.competitorService = competitorService;
        this.voteService = voteService;
        this.competitionService = competitionService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('USER_ADD_COMPETITOR')")
    public @ResponseBody ResponseEntity<GenericResponse> registerCompetitor(HttpServletRequest request, @RequestBody CompetitorRequestDto competitorRequestDto) {
        return prepareResponse(HttpStatus.OK, "Registered successfully", competitorService.registerCompetitor(request, competitorRequestDto));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_FETCH_COMPETITOR')")
    public ResponseEntity<GenericResponse> getAllCompetitor() {
        return prepareResponse(HttpStatus.OK, "Available competitors", competitorService.getAllCompetitors());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_FETCH_COMPETITOR')")
    public ResponseEntity<GenericResponse> getCompetitor(@PathVariable Long id) {
        return prepareResponse(HttpStatus.OK, "Successfully retrieved competitor", competitorService.getCompetitorById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE_COMPETITOR')")
    public ResponseEntity<GenericResponse> deleteCompetitor(@PathVariable Long id) {
        competitorService.deleteCompetitor(id);
        return prepareResponse(HttpStatus.OK, "Successfully deleted", null);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MODIFY_COMPETITOR')")
    public ResponseEntity<GenericResponse> updateCompetitor(HttpServletRequest request, @RequestBody CompetitorRequestDto competitorRequestDto) {
        return prepareResponse(HttpStatus.OK, "Competitor Updated successfully ", competitorService.updateCompetitor(request, competitorRequestDto));
    }

    @PostMapping("/vote")
    @PreAuthorize("hasAuthority('USER_MODIFY_COMPETITOR')")
    public ResponseEntity<GenericResponse> voteForCompetitor(
            @RequestParam("competitionId") Long competitionId,
            @RequestParam("competitorId") Long competitorId,
            HttpServletRequest request) {
        competitorService.voteForCompetitor(competitionId, competitorId, request);
        return prepareResponse(HttpStatus.OK, "Thanks for voting", null);
    }

    @GetMapping("/winner/{competitionId}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<GenericResponse> getCompetitionWinner(@PathVariable Long competitionId) {
        return prepareResponse(HttpStatus.OK, "Top 10 winners in this competition", competitorService.getWinner(competitionId));
    }

}
