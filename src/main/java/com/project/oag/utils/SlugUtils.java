package com.project.oag.utils;

public final class SlugUtils {
    private SlugUtils() {
    }

    public static String fromName(String firstName, String lastName, Long id) {
        String raw = String.join("-",
                firstName == null ? "" : firstName,
                lastName == null ? "" : lastName).trim();
        String slug = raw.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (slug.isBlank()) {
            slug = "user";
        }
        return id == null ? slug : slug + "-" + id;
    }

    public static String fromTitle(String title, Long id) {
        String slug = title == null ? "" : title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        if (slug.isBlank()) {
            slug = "item";
        }
        return id == null ? slug : slug + "-" + id;
    }
}
