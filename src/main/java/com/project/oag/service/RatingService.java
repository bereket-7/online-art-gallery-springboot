package com.project.oag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.oag.entity.Artwork;
import com.project.oag.entity.Rating;
import com.project.oag.repository.RatingRepository;

@Service
public class RatingService {
	
	  @Autowired
	  private RatingRepository ratingRepository;
	
	/*@Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private RatingRepository ratingRepository;
    
    @Autowired
    private RatingDto ratingDto;
    
    public RatingDto rateArtwork(Long id, int stars) {
    	  //Artwork artwork = artworkRepository.findById();
        	    /*if(artwork == null) {
    	        	throw new NotFoundException();}*/
        /*Rating rating = new Rating();
        rating.setArtwork(ratingDto.g;
        rating.setStars(stars);;
        rating = ratingRepository.save(rating);

        // Update the artwork's average rating
        artwork.getRatings().add(rating);
        artwork = artworkRepository.save(artwork);

        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(rating.getId());
        ratingDto.setStars(rating.getStars());
        return ratingDto;
    }
    public double getAverageRating(Long artworkId) {
        //Artwork artwork = artworkRepository.findById(artworkId);
        Artwork artwork = artworkRepository.findById(ratingDto.getArtworkName());
        Set<?> ratings = artwork.getRatings();

        if (ratings != null && !ratings.isEmpty()) {
            int totalStars = 0;
            for (Object rating : ratings) {
                totalStars += ((RatingDto) rating).getStars();
            }
            return (double) totalStars / ratings.size();
    }
        return 0;
    }*/
	
	/**
	  private final Map> ratings = new HashMap<>();
	    
	    public void addRating(Rating rating) {
	        List artworkRatings = ratings.computeIfAbsent(rating.getArtworkId(), k -> new ArrayList<>());
	        artworkRatings.add(rating);
	    }
	    
	    public List getRatingsForArtwork(String artworkId) {
	        return ratings.getOrDefault(artworkId, Collections.emptyList());
	    }
	    
	    public double getAvgRatingForArtwork(String artworkId) {
	        List artworkRatings = ratings.getOrDefault(artworkId, Collections.emptyList());
	        int totalRating = artworkRatings.stream().mapToInt(Rating::getRating).sum();
	        return (double)totalRating / artworkRatings.size();
	    }**/
	  
	  public Rating save(Rating rating) {
	        return ratingRepository.save(rating);
	    }

	    public List<Rating> findByArtwork(Artwork artwork) {
	        return ratingRepository.findByArtwork(artwork);
	    }

	    public Double getAverageRating(Artwork artwork) {
	        List<Rating> ratings = ratingRepository.findByArtwork(artwork);
	        if (ratings.isEmpty()) {
	            return null;
	        } else {
	            double sum = 0;
	            for (Rating rating : ratings) {
	                sum += rating.getStars();
	            }
	            return sum / ratings.size();
	        }
	    }
    
}
