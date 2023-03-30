package com.project.oag.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orderitems")
public class OrderItem {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(name = "quantity")
	    private @NotNull int quantity;

	    @Column(name = "price")
	    private @NotNull double price;

	    @Column(name = "created_date")
	    private Date createdDate;

	    @ManyToOne
	    @JsonIgnore
	    @JoinColumn(name = "order_id", referencedColumnName = "id")
	    private Order order;

	    @OneToOne
	    @JoinColumn(name = "artwork_id", referencedColumnName = "id")
	    private Artwork artwork;

		public OrderItem() {
			super();
			// TODO Auto-generated constructor stub
		}

		public OrderItem(Integer id, @NotNull int quantity, @NotNull double price, Date createdDate, Order order,
				Artwork artwork) {
			super();
			this.id = id;
			this.quantity = quantity;
			this.price = price;
			this.createdDate = createdDate;
			this.order = order;
			this.artwork = artwork;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
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

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public Order getOrder() {
			return order;
		}

		public void setOrder(Order order) {
			this.order = order;
		}

		public Artwork getArtwork() {
			return artwork;
		}

		public void setArtwork(Artwork artwork) {
			this.artwork = artwork;
		}
	    
	    
	    
	
}
