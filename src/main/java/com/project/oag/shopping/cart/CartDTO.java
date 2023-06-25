package com.project.oag.shopping.cart;

public class CartDTO {
    private Long id;
    private String artworkName;
    private int quantity;

    public CartDTO() {
    }

    public CartDTO(Long id, String artworkName, int quantity) {
        this.id = id;
        this.artworkName = artworkName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
