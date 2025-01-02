package com.grocery.GroceryStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.GroceryStore.DTO.InventoryDTO;
import com.grocery.GroceryStore.entity.Grocery;
import com.grocery.GroceryStore.service.GroceryService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private GroceryService groceryService;
	
	/*This method will generate CSRF token which will be used in 
	 * headers of every POST,PUT,DELETE requests*/
	@GetMapping("/csrf-token")
	public CsrfToken getCsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
	/*Method to add a single grocery entity to database*/
	@PostMapping("/add-grocery")
	public ResponseEntity<String> addGrocery(@RequestBody Grocery grocery){
		return groceryService.addGrocery(grocery);
	}
	
	/*Method to fetch all groceries from database*/
	@GetMapping("view-groceries/{id}")
	public ResponseEntity<Grocery> viewAllGroceries(@PathVariable int id ){
		return groceryService.findById(id);
	}
	
	/*Method to fetch grocery by id database*/
	@GetMapping("view-grocery")
	public ResponseEntity<List<Grocery>> viewGrocery(){
		return groceryService.findAll();
	}
	
	/*Method to delete a grocery based on id*/
	@DeleteMapping("delete-grocery/{id}")
	public ResponseEntity<Object> deleteGrocery(@PathVariable int id) {
		return groceryService.deleteGrocery(id);
	
	}
	
	/*Method to update a grocery based on id*/
	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateEmployee(@RequestBody Grocery grocery, @PathVariable int id) {
		return groceryService.updateGrocery(grocery, id);
	}
	
	/*Method to increase or decrease inventory of a grocery based on its id*/
	@PutMapping("update-inventory/{id}")
	public ResponseEntity<Object> updateInventory(@RequestBody InventoryDTO inventoryDTO, @PathVariable int id) {
		return groceryService.updateInventory(inventoryDTO.getQuantity(), id);
	}
}
