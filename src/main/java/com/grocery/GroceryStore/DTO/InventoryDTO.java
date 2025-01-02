package com.grocery.GroceryStore.DTO;

import org.springframework.stereotype.Component;

/*Date Transfer Object that stores quantity of grocery item to add 
 * or subtract from existing quantity*/
@Component
public class InventoryDTO {

	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
