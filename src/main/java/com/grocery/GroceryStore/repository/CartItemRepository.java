package com.grocery.GroceryStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grocery.GroceryStore.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	Optional<CartItem> findBygroceryId(int groceryId);

	Optional<List<CartItem>> findBycartId(int cartId);

}
