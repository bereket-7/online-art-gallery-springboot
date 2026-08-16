package com.project.oag.app.controller;

import com.project.oag.app.service.CollectionService;
import com.project.oag.common.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.oag.utils.Utils.prepareResponse;

@RestController
@RequestMapping("api/v1/collections")
@Tag(name = "Collections")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping
    public ResponseEntity<GenericResponse> list(@RequestParam(defaultValue = "false") boolean featuredOnly) {
        return prepareResponse(HttpStatus.OK, "Collections retrieved", collectionService.list(featuredOnly));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<GenericResponse> getBySlug(@PathVariable String slug) {
        return prepareResponse(HttpStatus.OK, "Collection retrieved", collectionService.getBySlug(slug));
    }
}
