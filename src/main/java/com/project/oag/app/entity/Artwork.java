package com.project.oag.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.oag.app.dto.ArtworkStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Artwork", indexes = {
        @Index(name = "idx_artwork_artwork_category", columnList = "ARTWORK_CATEGORY, STATUS")
})
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "ARTWORK_NAME")
    private String artworkName;

    @Column(name = "ARTWORK_DESCRIPTION")
    private String artworkDescription;

    @Column(name = "ARTWORK_CATEGORY")
    private String artworkCategory;

    @ElementCollection
    @Column(name = "IMAGE")
    private List<String> imageUrls;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "SIZE")
    private String size;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ArtworkStatus status;

    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @UpdateTimestamp
    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @ManyToOne
    @JoinColumn(name = "ARTIST_ID")
    private User artist;

    @JsonIgnore
    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Cart> carts;

    public Artwork(String filename, String string) {
    }
}