package com.project.oag.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "total_price")
    private Double totalPrice;
    
    @Column(name="currency")
    private String currency;
    
    @Column(name="intent")
    private String intent;
    
    @Column(name="method")
    private String method;
    
    @Column(name="description")
    private String description;

    @Column(name = "session_id")
    private String sessionId;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderItem> orderItem;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
	
    public Order() {
    }
    
    public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public String getIntent() {
		return intent;
	}


	public void setIntent(String intent) {
		this.intent = intent;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public List<OrderItem> getOrderItems() {
        return orderItem;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItem = orderItems;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
