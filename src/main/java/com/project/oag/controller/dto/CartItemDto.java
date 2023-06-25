package com.project.oag.controller.dto;

import com.project.oag.artwork.Artwork;
import com.project.oag.shopping.cart.Cart;

import jakarta.validation.constraints.NotNull;

public class CartItemDto {
	 	private Long id;
	    private @NotNull Integer quantity;
	    private @NotNull Artwork artwork;

	    public CartItemDto() {
	    }

	    public CartItemDto(Cart cart) {
	        this.setId(cart.getId());
	        this.setQuantity(cart.getQuantity());
	        this.setArtwork(cart.getArtwork());
	    }

	    @Override
	    public String toString() {
	        return "CartDto{" +
	                "id=" + id +
	                ", quantity=" + quantity +
	                ", artworkName=" + artwork.getArtworkName() +
	                '}';
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public Artwork getArtwork() {
			return artwork;
		}

		public void setArtwork(Artwork artwork) {
			this.artwork = artwork;
		}
}
