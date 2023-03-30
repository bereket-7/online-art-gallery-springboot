package com.project.oag.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import jakarta.persistence.Transient;

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
	@Lob
	@Column(name = "artwork_photo", nullable = false)
    private byte[] artworkPhoto;
	/*
    @Column(name = "artwork_photo", nullable = true, length = 64)
    private String artworkPhoto;
	
	
	@Lob
	@Column(columnDefinition = "MEDIUMBLOB")
	private String image;*/
	
	@Column(name = "price")
	private int price;
/**
	@Column(name = "upload_time", nullable = false)
	private LocalDateTime timestamp;**/
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;
	
	@Column(name = "artist_name")
	private String artistName;
	
	@Column(name = "size")
	private String size;
	
	@Column(name = "status")
	private String status = "pending";

	@OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    //private Set<?> ratings = new HashSet<>();
	private List<Rating> ratings;
	
	public Artwork() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Artwork(String artworkName, String artworkDescription, String artworkCategory, byte[] artworkPhoto,
			int price, Date createDate, String artistName, String size, String status, List<Rating> ratings) {
		super();
		this.artworkName = artworkName;
		this.artworkDescription = artworkDescription;
		this.artworkCategory = artworkCategory;
		this.artworkPhoto = artworkPhoto;
		this.price = price;
		this.createDate = createDate;
		this.artistName = artistName;
		this.size = size;
		this.status = status;
		this.ratings = ratings;
	}



	public Artwork(String artworkName, String artworkDescription, String artworkCategory, byte[] artworkPhoto,
			int price, LocalDateTime timestamp, String status, String artistName) {
		// TODO Auto-generated constructor stub
	}

	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
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


	public byte[] getArtworkPhoto() {
		return artworkPhoto;
	}

	public void setArtworkPhoto(byte[] artworkPhoto) {
		this.artworkPhoto = artworkPhoto;
	}

	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getArtistName() {
		return artistName;
	}


	public void setArtistName(String artistName) {
		this.artistName = artistName;
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
	
    @Transient
    public String getPhotosImagePath() {
        if (artworkPhoto == null || id == null) return null;
         
        return "/arts/" + id + "/" + artworkPhoto;
    }


	@Override
	public String toString() {
		return "Artwork [id=" + id + ", artworkName=" + artworkName + ", artworkDescription=" + artworkDescription
				+ ", artworkCategory=" + artworkCategory + ", artworkPhoto=" + Arrays.toString(artworkPhoto)
				+ ", price=" + price + ", createDate=" + createDate + ", artistName=" + artistName + ", status="
				+ status + ", ratings=" + ratings + "]";
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
