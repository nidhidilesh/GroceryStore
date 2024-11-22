package com.grocery.GroceryStore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartItemId;
	private int groceryId;
	private int groceryQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
	private Cart cart;
	
	public CartItem() {
	}
	
	public CartItem(int groceryId, int groceryQuantity) {
		super();
		this.groceryId = groceryId;
		this.groceryQuantity = groceryQuantity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public int getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getGroceryId() {
		return groceryId;
	}
	public void setGroceryId(int groceryId) {
		this.groceryId = groceryId;
	}
	public int getGroceryQuantity() {
		return groceryQuantity;
	}
	public void setGroceryQuantity(int groceryQuantity) {
		this.groceryQuantity = groceryQuantity;
	}
	
	
}
