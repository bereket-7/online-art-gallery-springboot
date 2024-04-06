package com.project.oag.app.controller;

import com.project.oag.app.dto.ArtworkRequestDto;
import com.project.oag.app.model.Artwork;
import com.project.oag.app.model.User;
import com.project.oag.app.repository.UserRepository;
import com.project.oag.app.service.ArtworkService;
import com.project.oag.common.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("api/artwork")
public class ArtworkController {
	private final ArtworkService artworkService;
	private final UserRepository userRepository;
	public ArtworkController(ArtworkService artworkService, UserRepository userRepository) {
		super();
		this.artworkService = artworkService;
        this.userRepository = userRepository;
    }

	@PostMapping("/upload")
	public ResponseEntity<GenericResponse> saveArtwork(HttpServletRequest request, @RequestBody ArtworkRequestDto artworkRequestDto) {
		return artworkService.saveArtwork(request,artworkRequestDto);
	}
	 @GetMapping("/{id}")
	 public ResponseEntity<Artwork> getArtwork(@PathVariable Long id, Model model) {
	     Optional<Artwork> artwork = artworkService.getArtworkById(id);

	     if (artwork == null) {
	         return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	     }
    return new ResponseEntity<>(artwork.get(), HttpStatus.OK);
	 }
	 @GetMapping
	 public ResponseEntity<List<Artwork>> getAllArtwork() {
	     List<Artwork> artworkList = artworkService.getAllArtworks();

	     if (artworkList == null) {
			 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		 }
    return new ResponseEntity<>(artworkList, HttpStatus.OK);
	 }
	@GetMapping("/category/{category}")
	//@PreAuthorize("hasRole('MANAGER','ARTIST','CUSTOMER')")
	public ResponseEntity<List<Artwork>> searchByCategory(@PathVariable("category") String artworkCategory) {
		List<Artwork> artworks = artworkService.getArtworkByCategory(artworkCategory);
		if (artworks == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(artworks, HttpStatus.OK);
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
	@GetMapping("/pending")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<List<Artwork>> getPendingArtworks() {
		List<Artwork> pendingArtworkList = artworkService.getPendingArtworks();

		if (pendingArtworkList == null || pendingArtworkList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(pendingArtworkList, HttpStatus.OK);
	}
	@GetMapping("/accepted")
	public ResponseEntity<List<Artwork>> getAcceptedArtworks() {
		List<Artwork> acceptedArtworkList = artworkService.getAcceptedArtworks();

		if (acceptedArtworkList == null || acceptedArtworkList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(acceptedArtworkList, HttpStatus.OK);
	}
	@GetMapping("/rejected")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<List<Artwork>> getRejectedArtworks()  {
		List<Artwork> rejectedArtworkList = artworkService.getRejectedArtworks();
		if (rejectedArtworkList == null || rejectedArtworkList.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(rejectedArtworkList, HttpStatus.OK);
	}
	    @PutMapping("/{id}/accept")
		@PreAuthorize("hasRole('MANAGER')")
	    public ResponseEntity<String> acceptArtwork(@PathVariable Long id) {
	        boolean accepted = artworkService.acceptArtwork(id);
	        if (accepted) {
	            return ResponseEntity.ok("Artwork with ID " + id + " has been accepted");
	        } else {
	            return ResponseEntity.badRequest().body("Artwork with ID " + id + " was not found or is not in pending list");
	        }
	    }
	    @PutMapping("/{id}/reject")
		@PreAuthorize("hasRole('MANAGER')")
	    public ResponseEntity<String> rejectArtwork(@PathVariable Long id) {
	        boolean rejected = artworkService.rejectArtwork(id);
	        if (rejected) {
	            return ResponseEntity.ok("Artwork with ID " + id + " has been Rejected due to the reason it does not satisfy company standard");
	        } else {
	            return ResponseEntity.badRequest().body("Artwork with ID " + id + " was not found or is not in pending status");
	        }
	    }
	    @GetMapping("/recent")
		//@PreAuthorize("hasRole('MANAGER','ARTIST','CUSTOMER')")
	    public ResponseEntity<List<Artwork>> getRecentArtworks() {
	        List<Artwork> artworks = artworkService.getRecentArtworks();
	        return new ResponseEntity<>(artworks, HttpStatus.OK);
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
	@GetMapping("/my-artworks")
	public ResponseEntity<List<Artwork>> getLoggedArtistArtworks() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedArtist = (User) authentication.getPrincipal();
		List<Artwork> artistArtworks = artworkService.getArtworksForLoggedArtist(loggedArtist);
		return ResponseEntity.ok(artistArtworks);
	}
}
