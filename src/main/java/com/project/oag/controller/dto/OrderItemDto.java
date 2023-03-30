package com.project.oag.controller.dto;

import jakarta.validation.constraints.NotNull;

public class OrderItemDto {

    private @NotNull double price;
    private @NotNull int quantity;
    private @NotNull int orderId;
    private @NotNull int artworkId;

    public OrderItemDto () {}

	public OrderItemDto(@NotNull double price, @NotNull int quantity, @NotNull int orderId, @NotNull int artworkId) {
		super();
		this.price = price;
		this.quantity = quantity;
		this.orderId = orderId;
		this.artworkId = artworkId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getArtworkId() {
		return artworkId;
	}

	public void setArtworkId(int artworkId) {
		this.artworkId = artworkId;
	}
    

}
