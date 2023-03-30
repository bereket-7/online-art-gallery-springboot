package com.project.oag.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.entity.Artwork;
import com.project.oag.service.ArtworkService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/upload_artwork")
public class ArtworkController {
	@Value("${uploadDir}")
	private String uploadFolder;
	@Autowired
	private ArtworkService artworkService;
	public ArtworkController(ArtworkService artworkService) {
		super();
		this.artworkService = artworkService;
	}
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@PostMapping("/image/saveImageDetails")
	public @ResponseBody ResponseEntity<?> registerArtwork(@RequestParam("artworkName") String artworkName,@RequestParam("artworkDescription") String artworkDescription,
			@RequestParam("artworkCategory") String artworkCategory,@RequestParam("price") int price,@RequestParam("artistName") String artistName,@RequestParam("status") String status, Model model, HttpServletRequest request
			,final @RequestParam("artworkPhoto") MultipartFile file) {
		try {
			//String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
			}
			String[] artwork_names = artworkName.split(",");
			String[] artwork_descriptions = artworkDescription.split(",");
			Date createDate = new Date();
			log.info("Artwork Name: " + artwork_names[0]+" "+filePath);
			log.info("Description: " + artwork_descriptions[0]);
			log.info("Artist Name: " + artistName);
			log.info("Category: " + artworkCategory);
			log.info("Price: " + price);
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
			Artwork artwork = new Artwork();
			artwork.setArtworkName(artwork_names[0]);
			artwork.setArtworkPhoto(imageData);
			artwork.setPrice(price);
			artwork.setArtworkDescription(artwork_descriptions[0]);
			artwork.setCreateDate(createDate);
			artworkService.saveArtwork(artwork);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		}
		
		@GetMapping("/image/display/{id}")
		@ResponseBody
		void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Artwork> artwork)
				throws ServletException, IOException {
			log.info("Id :: " + id);
			artwork = artworkService.getArtworkById(id);
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(artwork.get().getArtworkPhoto());
			response.getOutputStream().close();
		}
	

		@GetMapping("/image/imageDetails")
		String showProductDetails(@RequestParam("id") Long id, Optional<Artwork> artwork, Model model) {
			try {
				log.info("Id :: " + id);
				if (id != 0) {
					artwork = artworkService.getArtworkById(id);
				
					log.info("products :: " + artwork);
					if (artwork.isPresent()) {
						model.addAttribute("id", artwork.get().getId());
						model.addAttribute("description", artwork.get().getArtworkDescription());
						model.addAttribute("name", artwork.get().getArtworkName());
						model.addAttribute("price", artwork.get().getPrice());
						return "imagedetails";
					}
					return "redirect:/artworks";
				}
			return "redirect:/artworks";
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/artworks";
			}	
		}
		@GetMapping("/image/show")
		String show(Model map) {
			List<Artwork> images = artworkService.getAllImages();
			map.addAttribute("images", images);
			return "images";
		}
	
	
	/**
	@ModelAttribute("artwork")
    public ArtworkDto artworkDto() {
        return new ArtworkDto();
    }*/
	
	@GetMapping
	public String showUploadForm() {
		return "upload_artwork";
	}
	/**
	@PostMapping
	public String uploadArtwork(@ModelAttribute("artwork") ArtworkDto artworkDto) {
		artworkService.uploadArtwork(artworkDto);
		return "redirect:/upload_artwork?successfully uploaded and submitted for approval,thanks.";
	}**/
	
	/*@PostMapping()
	public String uploadArtwor(@ModelAttribute("artwork") ArtworkDto artworkDto,@RequestParam("artworkPhoto") MultipartFile multipartFile) {
		artworkService.saveArtwork(artworkDto, multipartFile);
		return "redirect:/upload_artwork?successfully uploaded and submitted for approval,thanks.";
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteArtwork(@PathVariable Long id) {
	      Artwork artwork = artworkRepository.findById(id).orElse(null);

	        if (artwork == null) {
	            return ResponseEntity.notFound().build();
	    }
	        boolean isConfirmed = false;
	        if (isConfirmed) {
	            artworkRepository.delete(artwork);
	            return ResponseEntity.ok().build();
	        }
	        else {
	            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deletion cancelled.");
	        }
	}
	
	   @GetMapping("/category/{category}")
	   public List<?> getByCategory(@PathVariable String category) {
	       return artworkService.searchByCategory(category);
	   }*/
	   /*
	    @PostMapping("/upload")
	    public String uploadArtwork(@RequestParam("file") MultipartFile file,
	                                @RequestParam("title") String title,
	                                Model model) throws IOException {
	        Artwork artwork = new Artwork();
	        artwork.setArtworkPhoto(file.getBytes());
	        artworkRepository.save(artwork);
	        
	        model.addAttribute("artworks", artworkRepository.findAll());
	        return "gallery";
	    }

	    @GetMapping("/artworks")
	    public String displayArtwork(Model model) {
	        model.addAttribute("artworks", artworkRepository.findAll());
	        return "artwork";
	    }*/

}
           

