package com.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expense.model.Category;
import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	
	/**
     * finder method to retrieve the categories by user id
     * @param userId
     * @return list
     * */
	List<Category> findByUserId(Long userId);
	
	
	/**
     * finder method fetch the category by user id and category id
     * @param id, categoryId
     * @return Optional<Category>
     * */
	Optional<Category> findByUserIdAndCategoryId(Long id, String categoryId);
	
	
	/**
     * It checks whether category us present or not by userid and category name
     * @param name, userId
     * @return boolean
     * */
	boolean existsByNameAndUserId(String name, Long userId);
	
	/**
     * it retrieves the category by name and userid
     * @param categoryname, userId
     * @return Optional of categoryEntity
     * */
	Optional<Category> findByNameAndUserId(String name, Long userId);
}
