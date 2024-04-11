package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPETITOR")
public class Competitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "IMAGE")
    private String imageUrl;

    @Column(name = "ARTWORK_DESCRIPTION")
    private String artworkDescription;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "VOTE")
    private int voteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPETITION_ID")
    @JsonIgnore
    private Long competitionId;

    @OneToMany(mappedBy = "competitorId")
    private List<Vote> votes;

    @JsonIgnoreProperties({"notifications","artworks"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTIST_ID")
    private Long artistId;
}
