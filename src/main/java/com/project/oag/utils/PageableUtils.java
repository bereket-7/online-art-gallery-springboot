package com.project.oag.utils;

import com.project.oag.app.dto.PageableDto;
import org.springframework.data.domain.Page;

public class PageableUtils {
    public static PageableDto preparePageInfo(Page page) {
        return PageableDto.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }
}
