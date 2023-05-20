package com.project.oag.entity;

import java.util.Date;
import java.util.List;

import com.project.oag.repository.RatingRepository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name="Artwork")
public class Artwork {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	 @Column(nullable=true)
	private String artworkName;

	 @Column(nullable=true)
    private String artworkDescription;
	
    @Column(nullable=true)
    private String artworkCategory;
	
	@Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
	
	private int price;

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;
	
    @Column(nullable=true)
    private int artistId;
	 
    @Column(nullable=true)
	private String size;
	
    @Column(nullable=true)
	private String status;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    

	public Artwork(String artworkName, String artworkDescription, String artworkCategory, byte[] image, int price,
			Date createDate, int artistId, String size, String status, List<Rating> ratings) {
		super();
		this.artworkName = artworkName;
		this.artworkDescription = artworkDescription;
		this.artworkCategory = artworkCategory;
		this.image = image;
		this.price = price;
		this.createDate = createDate;
		this.artistId = artistId;
		this.size = size;
		this.status = status;
		this.ratings = ratings;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Artwork() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Double getAverageRating(RatingRepository ratingRepository) {
	    return ratingRepository.findAverageRatingByArtworkId(id);
	}


	public Artwork(String filename, String string) {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArtworkName() {
		return artworkName;
	}

	public void setArtworkName(String artworkName) {
		this.artworkName = artworkName;
	}

	public String getArtworkDescription() {
		return artworkDescription;
	}

	public void setArtworkDescription(String artworkDescription) {
		this.artworkDescription = artworkDescription;
	}

	public String getArtworkCategory() {
		return artworkCategory;
	}

	public void setArtworkCategory(String artworkCategory) {
		this.artworkCategory = artworkCategory;
	}


	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}	

	
}
