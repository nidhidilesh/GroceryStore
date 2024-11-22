package com.grocery.GroceryStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.GroceryStore.entity.Grocery;
import com.grocery.GroceryStore.entity.Cart;
import com.grocery.GroceryStore.service.CartService;
import com.grocery.GroceryStore.service.GroceryService;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private GroceryService groceryService;
	
	@Autowired
	private CartService cartService;
	
	/*To get available grocery items */
	@GetMapping("/view-available-groceries")
	public ResponseEntity<List<Grocery>> viewAvailableGroceryItems() {
		return groceryService.findAvailableGroceryItems();
	}
	
	/*To add items to cart */
	@PostMapping("/add-items")
	public ResponseEntity<String> addItemsToCart(@RequestBody Cart cart) {
		return cartService.addItemsToCart(cart.getUserId(), cart.getCartItemList());
	}
}
