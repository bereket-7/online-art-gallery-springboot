package com.project.oag.app.controller;

import com.project.oag.app.service.DiscoveryService;
import com.project.oag.common.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/discovery")
public class DiscoveryController {

    private final DiscoveryService discoveryService;

    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping("/collections")
    public ResponseEntity<GenericResponse> getCollections(@RequestParam(defaultValue = "false") boolean featuredOnly) {
        var collections = featuredOnly ? discoveryService.getFeaturedCollections() : discoveryService.getAllCollections();
        return prepareResponse(HttpStatus.OK, "Collections retrieved", collections);
    }

    @GetMapping("/trending")
    public ResponseEntity<GenericResponse> getTrending(@RequestParam(defaultValue = "10") int limit) {
        return prepareResponse(HttpStatus.OK, "Trending artworks", discoveryService.getTrendingArtworks(limit));
    }
}
