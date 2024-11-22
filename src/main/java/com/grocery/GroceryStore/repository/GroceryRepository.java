package com.grocery.GroceryStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grocery.GroceryStore.entity.Grocery;
@Repository
public interface GroceryRepository extends JpaRepository<Grocery, Integer>{

}
