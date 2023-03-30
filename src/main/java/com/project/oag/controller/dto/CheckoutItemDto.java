package com.project.oag.controller.dto;

public class CheckoutItemDto {
	 	private String artworkName;
	    private int  quantity;
	    private double price;
	    private long artworkId;
	    private int userId;

	    public CheckoutItemDto() {}

		public CheckoutItemDto(String artworkName, int quantity, double price, long artworkId, int userId) {
			super();
			this.artworkName = artworkName;
			this.quantity = quantity;
			this.price = price;
			this.artworkId = artworkId;
			this.userId = userId;
		}

		public String getArtworkName() {
			return artworkName;
		}

		public void setArtworkName(String artworkName) {
			this.artworkName = artworkName;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public long getArtworkId() {
			return artworkId;
		}

		public void setArtworkId(long artworkId) {
			this.artworkId = artworkId;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		} 
}
