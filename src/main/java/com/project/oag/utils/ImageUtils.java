package com.project.oag.utils;

import com.project.oag.exceptions.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


public class ImageUtils {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/jpg"};
    private static final Pattern FILENAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    @Value("${baseURL}")
    private static String baseUrl;

    public static List<String> saveImagesAndGetUrls(List<MultipartFile> files) throws IOException {

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            // File size validation
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new GeneralException("File size exceeds limit of 5 MB");
            }

            // File type validation
            String contentType = file.getContentType();
            boolean isValidType = false;
            for (String allowedType : ALLOWED_FILE_TYPES) {
                if (allowedType.equals(contentType)) {
                    isValidType = true;
                    break;
                }
            }
            if (!isValidType) {
                throw new GeneralException("Invalid file type");
            }

            // Filename sanitization
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (!FILENAME_PATTERN.matcher(fileName).matches()) {
                throw new GeneralException("Invalid filename");

            }
            String uniqueFileName = UUID.randomUUID() + "_" + fileName;

            // Safe storage location
            String uploadDir = "uploads/images";

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path imagePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), imagePath);

            String imageUrl = baseUrl + "/images/" + uniqueFileName;
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }
}