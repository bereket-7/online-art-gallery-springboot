package com.project.oag.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "collection_artwork")
public class CollectionArtwork {
    @EmbeddedId
    private CollectionArtworkId id = new CollectionArtworkId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artworkId")
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;
}
