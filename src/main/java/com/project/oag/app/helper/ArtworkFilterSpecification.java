package com.project.oag.app.helper;

import com.project.oag.app.model.Artwork;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArtworkFilterSpecification {
    public static Specification<Artwork> searchArtworks(String artworkCategory,
                                                        String artworkName,
                                                        BigDecimal price,
                                                        String sortBy,
                                                        LocalDateTime fromDate,
                                                        LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (artworkCategory != null && !artworkCategory.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("artworkName")), "%" + artworkCategory.toLowerCase() + "%"));
            }
            if (artworkName != null && !artworkName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("artworkName")), "%" + artworkName.toLowerCase() + "%"));
            }
            if (price != null) {
                predicates.add(criteriaBuilder.equal(root.get("price"), price));
            }

            if (fromDate != null || toDate != null) {
                if (fromDate != null && toDate != null) {
                    predicates.add(criteriaBuilder.between(root.get("creationDate"), fromDate, toDate));
                } else if (fromDate != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), fromDate));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), toDate));
                }
            }

            if (sortBy != null && !sortBy.isEmpty()) {
                if (sortBy.equals("creationDate")) {
                    query.orderBy(criteriaBuilder.desc(root.get("creationDate")));
                }
                else if (sortBy.equals("ratings")) {
                    query.orderBy( criteriaBuilder.asc(root.get("ratings")));
                }
                else {
                    query.orderBy(criteriaBuilder.desc(root.get(sortBy)), criteriaBuilder.desc(root.get("price")));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
