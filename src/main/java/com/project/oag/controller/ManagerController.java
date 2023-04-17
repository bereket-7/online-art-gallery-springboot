package com.project.oag.controller;

public class ManagerController {

	/* @Autowired
	 private ArtworkRepository artworkRepository;
	 
	   @GetMapping("/pending-artwork")
	   public ModelAndView viewPendingPhotos() {
	       Artwork pendingArtworks = artworkRepository.findByStatus("pending");
	       ModelAndView modelAndView = new ModelAndView("pending-artwork");
	       modelAndView.addObject("Pending Artworks", pendingArtworks);
	       return modelAndView;
	   }
	   
	   @PostMapping("/verify-artwork")
	   public String approvePhoto(Long Id) {
	       Artwork artwork = artworkRepository.findById(Id).get();
	       artwork.setStatus("verified");
	       artworkRepository.save(artwork);
	       return "redirect:/artwork";
	   }
	 
	 */
}
