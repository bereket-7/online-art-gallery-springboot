package com.project.oag.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long ID;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "ARTWORK_DESCRIPTION")
    private String artworkDescription;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "VOTE")
    private int vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPETITION_ID")
    @JsonIgnore
    private Competition competition;

    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
    private List<Vote> votes;

    //@JsonIgnoreProperties({"notifications","artworks"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
    public void addVote(Vote vote) {
        votes.add(vote);
        vote.setCompetitor(this);
    }
    public void removeVote(Vote vote) {
        votes.remove(vote);
        vote.setCompetitor(null);
    }
    public void incrementVoteCount() {
        vote++;
    }
}
