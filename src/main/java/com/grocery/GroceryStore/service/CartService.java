package com.grocery.GroceryStore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grocery.GroceryStore.entity.Grocery;
import com.grocery.GroceryStore.exception.GlobalExceptionHandler;
import com.grocery.GroceryStore.exception.ResourceNotFoundException;
import com.grocery.GroceryStore.model.PaymentType;
import com.grocery.GroceryStore.entity.Cart;
import com.grocery.GroceryStore.entity.CartItem;
import com.grocery.GroceryStore.repository.CartItemRepository;
import com.grocery.GroceryStore.repository.CartRepository;
import com.grocery.GroceryStore.repository.GroceryRepository;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private GlobalExceptionHandler exceptionHandler;
	
	@Autowired
	private GroceryRepository groceryRepository;
	
//	@Autowired
//	private PaymentType paymentType;
	
	/*Method to find user if it exists otherwise create a new record in cart table*/
	public Cart findOrCreateByuserId(int userId) {
        return cartRepository.findByuserId(userId)
                             .orElseGet(() -> {
                                 Cart newCart = new Cart();
                                 newCart.setUserId(userId);
                                 return cartRepository.save(newCart);
                             });
    }
	
	/* Method to add items to cart
	 * and decrement grocery quantity from grocery table based on 
	 * the quantity of grocery item added to cart */
	public ResponseEntity<String> addItemsToCart(int userId, List<CartItem> items) {
		
		try {
		Cart cart = findOrCreateByuserId(userId);
		int cartIdFromDb = cart.getCartId();
		System.out.println("cartIdfromdb = " + cartIdFromDb);
		
		for (CartItem itemFromRequest : items) {
			boolean saved = false;
			CartItem cartItem = new CartItem();
			Optional<List<CartItem>> cartItemsFromDbOptional = cartItemRepository.findBycartId(cartIdFromDb);
			if(!cartItemsFromDbOptional.get().isEmpty()) {
				List<CartItem> cartItemsfromDb = cartItemsFromDbOptional.get();
				
				for(CartItem cartItemFromDb : cartItemsfromDb) {
					if(cartItemFromDb.getGroceryId() != itemFromRequest.getGroceryId()) {
						System.out.println("continuing");
						continue;
					}
					else {
						System.out.println("doing 1");
						/*Idea is - a particular user cart should not have multiple entries of the same
						*grocery item. If the user updates quantity of a grocery already present in their 
						*cart, then the quantity of the grocery item in the cart gets updated.*/
						Optional<Grocery> groceryFromDb = groceryRepository.findById(cartItemFromDb.getGroceryId());
						int availableQuantityOfGroceryItem = groceryFromDb
								.orElseThrow(() -> new ResourceNotFoundException())
								.getQuantity();
						
						int quantityOfGroceryAfterAddingToCart = Math.subtractExact(availableQuantityOfGroceryItem, itemFromRequest.getGroceryQuantity());
						if(itemFromRequest.getGroceryQuantity() == 0) {
							throw new RuntimeException("Grocery you are trying to add to cart has no quantity. Please enter a positive number");
						}
						else if(availableQuantityOfGroceryItem >= 0 && quantityOfGroceryAfterAddingToCart >= 0) {
							 
							cartItem.setCartItemId(cartItemFromDb.getCartItemId());
							cartItem.setCart(cart);
							cartItem.setGroceryQuantity(Math.abs(Math.addExact(cartItemFromDb.getGroceryQuantity(), itemFromRequest.getGroceryQuantity())));
							cartItem.setGroceryId(itemFromRequest.getGroceryId());
							cart.getCartItemList().add(cartItem);
							cartRepository.save(cart);
							
							Grocery grocery = new Grocery();
							grocery.setName(groceryFromDb.get().getName());
							grocery.setPrice(groceryFromDb.get().getPrice());
							grocery.setQuantity(quantityOfGroceryAfterAddingToCart);
							grocery.setId(groceryFromDb.get().getId());
							groceryRepository.save(grocery);
							
							saved = true;
							
						}
						else {
							throw new RuntimeException("Sorry, Grocery " + groceryFromDb.get().getId() + " you are trying to add to cart is out of stock. Remaining quantity is " + groceryFromDb.get().getQuantity());
						}
					}
				}
				if(!saved)createNewEntry(cart, itemFromRequest, cartItem);
			}
			else {
				System.out.println("in else");
				if(!saved)createNewEntry(cart, itemFromRequest, cartItem);
			}
		}
		}catch (Exception e) {
			return exceptionHandler.handleOutOfStockException(e);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	public void createNewEntry(Cart cart, CartItem itemFromRequest, CartItem cartItem) {
		System.out.println("doing 2");
		
		Optional<Grocery> groceryFromDb = groceryRepository.findById(itemFromRequest.getGroceryId());
		
		Grocery grocery = new Grocery();
		int availableQuantityOfGroceryItem = Math.subtractExact(groceryFromDb.get().getQuantity(), itemFromRequest.getGroceryQuantity());
		if(itemFromRequest.getGroceryQuantity() == 0) {
			throw new RuntimeException("Grocery you are trying to add to cart has no quantity. Please enter a positive number");
		}
		else if(groceryFromDb.get().getQuantity() >= 0 && availableQuantityOfGroceryItem >= 0) {
			grocery.setQuantity(availableQuantityOfGroceryItem);
			grocery.setName(groceryFromDb.get().getName());
			grocery.setPrice(groceryFromDb.get().getPrice());
			grocery.setId(groceryFromDb.get().getId());
			groceryRepository.save(grocery);

			cartItem.setCart(cart);
			cartItem.setGroceryId(itemFromRequest.getGroceryId());
			cartItem.setGroceryQuantity(itemFromRequest.getGroceryQuantity());
			cart.getCartItemList().add(cartItem);
			cartRepository.save(cart);
			
		}
		else {
			throw new RuntimeException("Sorry, Grocery " + groceryFromDb.get().getName() + " you are trying to add to cart is out of stock. Remaining quantity is " + groceryFromDb.get().getQuantity());
		}
	}
	
	public void checkout(Optional<Integer> paymentTypeId) {
		
	}
	
//	public void setPaymentType(Optional<Integer> paymentTypeId) {
//		if(!paymentTypeId.isPresent()) {
//			paymentType.set
//		}
//	}
}
