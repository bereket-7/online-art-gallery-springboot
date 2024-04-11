package com.project.oag.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageUtils {
    @Value("${baseURL}")
    private static String baseUrl;
    public static List<String> saveImagesAndGetUrls(List<MultipartFile> files) {

        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            String uploadDir = "/resources/Images";

            Path imagePath = Paths.get(uploadDir + File.separator + uniqueFileName);
            Files.copy(file.getInputStream(), imagePath);



            String imageUrl = baseUrl + "/images" + uniqueFileName;
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

}
