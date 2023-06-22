package com.project.oag.competition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "competitor")
public class Competitor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	@Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;
	private String artDescription;
	private String category;
	private int vote;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competition_id", nullable = true)
	@JsonIgnore
	private Competition competition;
	@OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
	private List<Vote> votes;
	public Competitor() {
		super();
	}
	public Competitor(String firstName, String lastName, String email, String phone, byte[] image,
			String artDescription, String category, int vote, Competition competition) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.image = image;
		this.artDescription = artDescription;
		this.category = category;
		this.vote = vote;
		this.competition = competition;
	}

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
	public Competitor(String filename, String string) {
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getArtDescription() {
		return artDescription;
	}

	public void setArtDescription(String artDescription) {
		this.artDescription = artDescription;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}
	

}
