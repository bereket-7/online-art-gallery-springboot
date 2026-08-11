package com.project.oag.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CollectionArtworkId implements Serializable {
    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "artwork_id")
    private Long artworkId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionArtworkId that)) return false;
        return Objects.equals(collectionId, that.collectionId) && Objects.equals(artworkId, that.artworkId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionId, artworkId);
    }
}
