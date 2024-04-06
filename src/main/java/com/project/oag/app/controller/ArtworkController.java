package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.dto.ArtworkStatus;
import com.project.oag.app.model.Artwork;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("api/v1/artwork")
public class ArtworkController {
	private final ArtworkService artworkService;
    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @PostMapping("/upload")
	@PreAuthorize("hasAuthority('USER_ADD_ARTWORK')")
	public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request, @RequestBody ArtworkRequestDto artworkRequestDto) {
		return artworkService.saveArtwork(request,artworkRequestDto);
	}
	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
	public ResponseEntity<GenericResponse> getAllArtworks() {
		return artworkService.getAllArtworks();
	}
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
	public ResponseEntity<GenericResponse> getArtworkById(@PathVariable Long id) {
		return artworkService.getArtworkById(id);
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
	public ResponseEntity<GenericResponse> updateArtwork(@PathVariable Long id, @RequestBody ArtworkRequestDto artworkRequestDto) {
		return artworkService.updateArtwork(id,artworkRequestDto);
	}

	@GetMapping("/category")
	@PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
	public ResponseEntity<GenericResponse> getArtworksByCategory(@RequestParam(required = false) String artworkCategory) {
		return artworkService.getArtworkByArtworkCategory(artworkCategory);
	}
	@GetMapping("/priceRange")
	public ResponseEntity<List<Artwork>> getArtworksByPriceRange(@RequestParam("minPrice") int minPrice,
																 @RequestParam("maxPrice") int maxPrice) {
		try {
			List<Artwork> artworks = artworkService.getArtworksByPriceRange(minPrice, maxPrice);
			return new ResponseEntity<>(artworks, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN_DELETE_ARTWORK')")
	public ResponseEntity<GenericResponse> deleteArtwork(@PathVariable Long id) {
		   return artworkService.deleteArtwork(id);
	}

	@GetMapping("/status")
	@PreAuthorize("hasAuthority('ADMIN_FETCH_ARTWORK')")
	public ResponseEntity<GenericResponse> getArtworksByStatus(@RequestParam(required = false) ArtworkStatus status) {
		return artworkService.getArtworkByArtworkStatus(status);
	}

	@PatchMapping("/change/status/{id}")
	@PreAuthorize("hasAuthority('ADMIN_MODIFY_ARTWORK')")
	public ResponseEntity<GenericResponse> changeStatus(@PathVariable Long id, @RequestParam(required = false) ArtworkStatus status) {
		return artworkService.changeArtworkStatus(id,status);
	}

	@GetMapping("/recent")
	@PreAuthorize("hasAuthority('USER_FETCH_ARTWORK')")
	public ResponseEntity<GenericResponse> getRecentArtworks() {
		return artworkService.getRecentArtworks();
	}
	@GetMapping("/count-by-category")
	public ResponseEntity<Map<String, Integer>> getCountByCategory() {
		Map<String, Integer> countByCategory = artworkService.getCountByCategory();
		return ResponseEntity.ok(countByCategory);
	}
	@GetMapping("/search")
	public ResponseEntity<List<Artwork>> searchArtwork(
			@RequestParam("keyword") String keyword,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size
	) {
		try {
			List<Artwork> searchResults = artworkService.searchArtwork("%" + keyword + "%", page, size);
			return ResponseEntity.ok(searchResults);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/autocomplete")
	@PreAuthorize("hasRole('MANAGER','ARTIST','CUSTOMER')")
	public ResponseEntity<List<String>> autocomplete(@RequestParam("keyword") String keyword) {
		List<String> autocompleteResults = artworkService.getAutocompleteResults(keyword);
		return ResponseEntity.ok(autocompleteResults);
	}
	@GetMapping("/sort")
	public ResponseEntity<List<Artwork>> getSortedArtworks(@RequestParam("sortOption") String sortOption) {
		try {
			List<Artwork> artworks = artworkService.getSortedArtworks(sortOption);
			return new ResponseEntity<>(artworks, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/me")
	@PreAuthorize("hasAuthority('USER_FETCH_ARTWORK')")
	public ResponseEntity<GenericResponse> getLoggedArtistArtworks(HttpServletRequest request) {
		return artworkService.getLoggedArtistArtworks(request);
	}
}
