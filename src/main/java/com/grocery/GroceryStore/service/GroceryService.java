package com.grocery.GroceryStore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grocery.GroceryStore.entity.Grocery;
import com.grocery.GroceryStore.exception.GlobalExceptionHandler;
import com.grocery.GroceryStore.repository.GroceryRepository;

@Service
public class GroceryService {

	@Autowired
	private GroceryRepository groceryRepository;
	
	@Autowired
	private GlobalExceptionHandler exceptionHandler;
	
	@Autowired
	private RedisService redisService;
	
	public ResponseEntity<String> addGrocery(Grocery grocery) {
		groceryRepository.save(grocery);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<List<Grocery>> findAll() {
		List<Grocery> list = groceryRepository.findAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	public ResponseEntity<Grocery> findById(int id) {
		Grocery g = redisService.get("grocery_by_id_"+id, Grocery.class);
		if(g != null) {
			return new ResponseEntity<>(g,HttpStatus.OK);
		}
		else {
			Optional<Grocery> groceryOptional = groceryRepository.findById(id);
			if(groceryOptional.isPresent()) {
				redisService.set("grocery_by_id"+id, groceryOptional.get(), 100);
				return new ResponseEntity<>(groceryOptional.get(),HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	public ResponseEntity<Object> deleteGrocery(int id) {
		boolean groceryExistsforId = groceryRepository.existsById(id);
		if(groceryExistsforId) {
			groceryRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			Exception nfe = new RuntimeException("Record not found for the given ID");
			return exceptionHandler.handleNotFoundException(nfe);
		}
	}

	public ResponseEntity<Object> updateGrocery(Grocery grocery, int id) {
		Optional<Grocery> groceryById = groceryRepository.findById(id);
		if(!groceryById.isEmpty()) {
			grocery.setId(groceryById.get().getId());
			groceryRepository.save(grocery);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			Exception nfe = new RuntimeException("Record not found for the given ID");
			return exceptionHandler.handleNotFoundException(nfe);
		}
	}

	/*Method to get available grocery items */
	public ResponseEntity<List<Grocery>> findAvailableGroceryItems() {
		List<Grocery> availableGroceryItems = groceryRepository.findAll().stream().filter(grocery -> grocery.getQuantity()>0).collect(Collectors.toList());
		return new ResponseEntity<>(availableGroceryItems, HttpStatus.OK);
	}

	/*Method to update inventory details by adding or subtracting stock quantity*/
	public ResponseEntity<Object> updateInventory(int quantity, int id) {
		Optional<Grocery> groceryById = groceryRepository.findById(id);
		if(!groceryById.isEmpty()) {
			groceryById.get().setQuantity(Math.addExact(groceryById.get().getQuantity(), quantity));
			groceryRepository.save(groceryById.get());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			Exception nfe = new RuntimeException("Record not found for the given ID");
			return exceptionHandler.handleNotFoundException(nfe);
		}
	}

	

}
