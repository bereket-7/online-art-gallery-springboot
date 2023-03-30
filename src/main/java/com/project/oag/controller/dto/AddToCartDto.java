package com.project.oag.controller.dto;

import jakarta.validation.constraints.NotNull;
public class AddToCartDto {
    private Long id;
    private @NotNull Long artworkId;
    private @NotNull Integer quantity;

    public AddToCartDto() {
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "id=" + id +
                ", productId=" + artworkId +
                ", quantity=" + quantity +
                ",";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArtworkId() {
		return artworkId;
	}

	public void setArtworkId(Long artworkId) {
		this.artworkId = artworkId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
