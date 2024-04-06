package com.project.oag.app.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ArtworkRequestDto {
	private String artworkName;
	private String artworkDescription;
	private String artworkCategory;
	private int price;
	private String size;
	List<MultipartFile> imageFiles;

}
