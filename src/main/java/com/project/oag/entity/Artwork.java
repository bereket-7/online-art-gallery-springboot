package com.project.oag.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="artwork")
public class Artwork {
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "artwork_name", nullable = false,length=150)
	private String artworkName;
	
	@Column(name = "artwork_description", nullable = false)
    private String artworkDescription;
	
	@Column(name = "artwork_category", nullable = false,length=100)
    private String artworkCategory;
	

	@Column(name = "artwork_photo", nullable = true)
    private String artworkPhoto;
	
	@Column(name = "price")
	private int price;
	
    @Column(name = "create_date", nullable = false, columnDefinition = "DATE")
    private LocalDate createDate;
	
	@Column(name = "artist_id")
    private int artistId;
	 
	@Column(name = "size")
	private String size;
	
	@Column(name = "status")
	private String status;

	@OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    //private Set<?> ratings = new HashSet<>();
	private List<Rating> ratings;
	
	
	
	public int getArtistId() {
		return artistId;
	}



	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}



	public Artwork(Long id, String artworkName, String artworkDescription, String artworkCategory, String artworkPhoto,
			int price, LocalDate createDate, int artistId, String size, String status, List<Rating> ratings) {
		super();
		this.id = id;
		this.artworkName = artworkName;
		this.artworkDescription = artworkDescription;
		this.artworkCategory = artworkCategory;
		this.artworkPhoto = artworkPhoto;
		this.price = price;
		this.createDate = createDate;
		this.artistId = artistId;
		this.size = size;
		this.status = status;
		this.ratings = ratings;
	}



	public Artwork() {
		super();
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

	public String getArtworkPhoto() {
		return artworkPhoto;
	}

	public void setArtworkPhoto(String artworkPhoto) {
		this.artworkPhoto = artworkPhoto;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
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

	

	/**public Artwork(String artworkName, String artworkDescription, String artworkCategory,
			MultipartFile artworkPhoto, int price, String artistName, String Status, LocalDateTime timestamp) {
		// TODO Auto-generated constructor stub
	}**/
	
	/*
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private int price;
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String image;
	*/
}
