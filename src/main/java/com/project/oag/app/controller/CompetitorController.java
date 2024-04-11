package com.project.oag.app.controller;

import com.project.oag.app.dto.CompetitorRequestDto;
import com.project.oag.app.service.CompetitionService;
import com.project.oag.app.service.CompetitorService;
import com.project.oag.app.service.VoteService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
	public @ResponseBody ResponseEntity<GenericResponse> registerCompetitor(HttpServletRequest request,@RequestBody CompetitorRequestDto competitorRequestDto) {
		return competitorService.registerCompetitor(request,competitorRequestDto);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN_FETCH_COMPETITOR')")
	public ResponseEntity<GenericResponse> getAllCompetitor() {
		return competitorService.getAllCompetitors();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_FETCH_COMPETITOR')")
	public ResponseEntity<GenericResponse> getCompetitor(@PathVariable Long id) {
		return competitorService.getCompetitorById(id);
	}
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_DELETE_COMPETITOR')")
	public ResponseEntity<GenericResponse> deleteCompetitor(@PathVariable Long id) {
		return competitorService.deleteCompetitor(id);
	}
	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('USER_MODIFY_COMPETITOR')")
	public ResponseEntity<GenericResponse> updateCompetitor(HttpServletRequest request, @RequestBody CompetitorRequestDto competitorRequestDto) {
		return competitorService.updateCompetitor(request, competitorRequestDto);
	}
	@PostMapping("/vote")
	@PreAuthorize("hasAuthority('USER_MODIFY_COMPETITOR')")
	public ResponseEntity<GenericResponse> voteForCompetitor(
			@RequestParam("competitionId") Long competitionId,
			@RequestParam("competitorId") Long competitorId,
			HttpServletRequest request) {
		return competitorService.voteForCompetitor(competitionId,competitorId,request);
	}
	@GetMapping("/winner/{competitionId}")
	@PreAuthorize("hasRole('CUSTOMER','MANAGER')")
	public ResponseEntity<GenericResponse> getCompetitionWinner(@PathVariable Long competitionId) {
		return competitorService.getWinner(competitionId);
	}

}
