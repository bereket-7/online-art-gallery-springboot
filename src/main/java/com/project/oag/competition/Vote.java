package com.project.oag.competition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.oag.user.User;
import jakarta.persistence.*;
    @Entity
	@Table(name = "vote")
	public class Vote {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "competitor_id", nullable = false)
		@JsonIgnore
		private Competitor competitor;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "user_id", nullable = false)
		private User user;

		public Vote() {
		}

		public Vote(long id, Competitor competitor, User user) {
			this.id = id;
			this.competitor = competitor;
			this.user = user;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public Competitor getCompetitor() {
			return competitor;
		}

		public void setCompetitor(Competitor competitor) {
			this.competitor = competitor;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}


