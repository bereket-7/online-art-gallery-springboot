package com.project.oag.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.oag.common.FileUploadUtil;
import com.project.oag.controller.dto.ArtworkDto;
import com.project.oag.entity.Artwork;
import com.project.oag.service.ArtworkService;
import com.project.oag.service.UserService;

@RestController
@RequestMapping("/artwork")
@CrossOrigin("http://localhost:8080/")
public class ArtworkController {
	private String path = "src/main/resources/static/img/artwork-images/";

	@Autowired
	private ArtworkService artworkService;

	@Autowired
	private UserService userService;

	public ArtworkController(ArtworkService artworkService) {
		super();
		this.artworkService = artworkService;
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/artwork/saveArtwok")
	public @ResponseBody ResponseEntity<?> registerArtwork(@RequestParam("artworkName") String artworkName,
			@RequestParam("artworkDescription") String artworkDescription,
			@RequestParam("artworkCategory") String artworkCategory, @RequestParam("price") int price,
			@RequestParam("artistName") String artistName, @RequestParam("status") String status, Model model,
			HttpServletRequest request, final @RequestParam("artworkPhoto") MultipartFile file) {
		try {
			// String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName,
						HttpStatus.BAD_REQUEST);
			}
			String[] artwork_names = artworkName.split(",");
			String[] artwork_descriptions = artworkDescription.split(",");
			Date createDate = new Date();
			log.info("Artwork Name: " + artwork_names[0] + " " + filePath);
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
			return new ResponseEntity<>("Artwork Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/artwork/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Artwork> artwork)
			throws ServletException, IOException {
		log.info("Id :: " + id);
		artwork = artworkService.getArtworkById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(artwork.get().getArtworkPhoto());
		response.getOutputStream().close();
	}

	@GetMapping("/artwork/artworkDetails")
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

	@GetMapping("/artwork/show")
	String show(Model map) {
		List<Artwork> artworks = artworkService.getAllArtworks();
		map.addAttribute("artworks", artworks);
		return "artworks";
	}

	/*
	 * @GetMapping("/{id}")
	 * public ResponseEntity<Artwork> getPhoto(@PathVariable Long id) {
	 * return ResponseEntity.ok(artworkService.getPhoto(id));
	 * }
	 * 
	 * @PostMapping("/")
	 * public ResponseEntity<Artwork> createPhoto(@RequestBody Artwork artwork) {
	 * return ResponseEntity.ok(artworkService.createPhoto(artwork));
	 * }
	 * 
	 * @PutMapping("/{id}")
	 * public ResponseEntity<Artwork> updatePhoto(@PathVariable Long
	 * id, @RequestBody Artwork artwork) {
	 * return ResponseEntity.ok(artworkService.updatePhoto(id, artwork));
	 * }
	 * 
	 * @DeleteMapping("/{id}")
	 * public void deleteArtwork(@PathVariable Long id) {
	 * artworkService.deleteArtwork(id);
	 * }
	 * 
	 * @GetMapping("/all")
	 * public List<Artwork> getAllPhotos() {
	 * return artworkService.getAllArtworks();
	 * }
	 * 
	 * @DeleteMapping("/{id}")
	 * public void deleteArtwork(@PathVariable Long id) {
	 * artworkService.deleteArtwork(id);
	 * }
	 * 
	 * @GetMapping("/byCategory/{artworkCategory}")
	 * public List<Artwork> getArtworkByCategory(@PathVariable String
	 * artworkCategory) {
	 * return artworkService.getArtworkByCategory(artworkCategory);
	 * }
	 * 
	 * @GetMapping("/byArtistName/{artistName}")
	 * public List<Artwork> getPhotosByArtistName(@PathVariable String artistName) {
	 * return artworkService.getArtworksByArtistName(artistName);
	 * }
	 * 
	 * @GetMapping("/byArtistId/{artistId}")
	 * public List<Artwork> getPhotosByArtworkName(@PathVariable int artistId) {
	 * return artworkService.getArtworksByArtistId(artistId);
	 * }
	 * 
	 * @GetMapping("/priceRange")
	 * public List<Artwork> getArtworkByPriceRange(@RequestParam double
	 * minPrice, @RequestParam double maxPrice) {
	 * return artworkService.getArtworkByPriceRange(minPrice, maxPrice);
	 * }
	 * 
	 * @GetMapping("/pending")
	 * public List<Artwork> getPendingArtworks() {
	 * return artworkService.getPendingArtworks();
	 * }
	 * 
	 * @GetMapping("/accepted")
	 * public List<Artwork> getAcceptedArtworks() {
	 * return artworkService.getAcceptedArtworks();
	 * }
	 * 
	 * @GetMapping("/rejected")
	 * public List<Artwork> getRejectedArtworks() {
	 * return artworkService.getRejectedArtworks();
	 * }
	 * 
	 * @PutMapping("/{id}/accept")
	 * public ResponseEntity<String> acceptArtwork(@PathVariable Long id) {
	 * boolean accepted = artworkService.acceptArtwork(id);
	 * if (accepted) {
	 * return ResponseEntity.ok("Artwork with ID " + id + " has been accepted");
	 * } else {
	 * return ResponseEntity.badRequest().body("Artwork with ID " + id +
	 * " was not found or is not pending");
	 * }
	 * }
	 * 
	 * @PutMapping("/{id}/reject")
	 * public ResponseEntity<String> rejectArtwork(@PathVariable Long id) {
	 * boolean rejected = artworkService.rejectArtwork(id);
	 * if (rejected) {
	 * return ResponseEntity.ok("Artwork with ID " + id +
	 * " has been Rejected due to the reason it does not satisfy company standard");
	 * } else {
	 * return ResponseEntity.badRequest().body("Artwork with ID " + id +
	 * " was not found or is not in pending status");
	 * }
	 * }
	 * 
	 * @PutMapping("/{id}")
	 * public ResponseEntity<ArtworkDto> updateArtwork(@PathVariable Long
	 * id, @RequestBody ArtworkDto artworkDto) {
	 * ArtworkDto updatedArtworkDto = artworkService.updateArtwork(id, artworkDto);
	 * return ResponseEntity.ok(updatedArtworkDto);
	 * }
	 * 
	 * @GetMapping("/recent")
	 * public ResponseEntity<List<Artwork>> getRecentArtworks() {
	 * List<Artwork> artworks = artworkService.getRecentArtworks();
	 * return new ResponseEntity<>(artworks, HttpStatus.OK);
	 * }
	 * /*
	 * 
	 * @GetMapping("/{artworkId}/average-rating")
	 * public double getAverageRating(@PathVariable Long artworkId) {
	 * return artworkService.getAverageRating(artworkId);
	 * }
	 */

}
