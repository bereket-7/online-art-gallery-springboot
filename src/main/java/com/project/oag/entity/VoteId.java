package com.project.oag.entity;

import java.io.Serializable;
import java.util.Objects;

public class VoteId implements Serializable {
	  private Long userId;
	  private Long competitorId;
	public VoteId() {
		super();
		// TODO Auto-generated constructor stub
	}
	public VoteId(Long userId, Long competitorId) {
		super();
		this.userId = userId;
		this.competitorId = competitorId;
	}
	@Override
	public String toString() {
		return "VoteId [userId=" + userId + ", competitorId=" + competitorId + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(competitorId, userId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoteId other = (VoteId) obj;
		return Objects.equals(competitorId, other.competitorId) && Objects.equals(userId, other.userId);
	} 
}
