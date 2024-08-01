package com.expense.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.Dto.CategoryDTO;
import com.expense.Dto.UserDTO;
import com.expense.exceptions.ItemAlreadyExistsException;
import com.expense.exceptions.ResourceNotFoundException;
import com.expense.model.Category;
import com.expense.model.User;
import com.expense.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor //constructor Inection
public class CategoryServiceImpl implements CategoryService {
	
	
	private final CategoryRepository categoryRepository;
	
	private final UserService userService;

	/**
     * This is for reading the categories from database
     * @return list
     * */
	@Override
	public List<CategoryDTO> getAllCategories() {
		
		 List<Category> list = categoryRepository.findByUserId(userService.getLoggedInUser().getId());
		 return list.stream().map(category -> mapToDto(category)).collect(Collectors.toList());
	}

	/**
     * Mapper method to convert Category entity to Category DTO
     * @param categoryEntity
     * @return CategoryDTO
     * */
	private CategoryDTO mapToDto(Category category) {
		return CategoryDTO.builder()
		.categoryId(category.getCategoryId())
		.name(category.getName())
		.description(category.getDescription())
		.categoryIcon(category.getCategoryIcon())
		.createdAt(category.getCreatedAt())
		.updatedAt(category.getUpdatedAt())
		.user(mapToUserDto(category.getUser()))
		.build();
	}

	
	/**
     * Mapper method to convert User entity to User DTO
     * @param user
     * @return UserDTO
     * */
	private UserDTO mapToUserDto(User user) {
		// TODO Auto-generated method stub
		return UserDTO.builder()
				.email(user.getEmail())
				.name(user.getName())
				.build();
	}

	
	/**
     * This is for creating the new category
     * @param categoryDTO
     * @return CategoryDTO
     * */
	@Override
	public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
		boolean isCategoryPresent = categoryRepository.existsByNameAndUserId(categoryDTO.getName(), userService.getLoggedInUser().getId());
		if(isCategoryPresent) {
			throw new ItemAlreadyExistsException("Category is already present for the name: "+ categoryDTO.getName());
		}
		Category newCategory = mapToEntity(categoryDTO);
		newCategory = categoryRepository.save(newCategory);
		return mapToDto(newCategory);
	}

	/**
     * Mapper method to convert category dto to cateogry entity
     * @param categoryDTO
     * @return CategoryEntity
     * */
	private Category mapToEntity(CategoryDTO categoryDTO) {
		return Category.builder()
		.name(categoryDTO.getName())
		.description(categoryDTO.getDescription())
		.categoryIcon(categoryDTO.getCategoryIcon())
		.categoryId(UUID.randomUUID().toString())
		.user(userService.getLoggedInUser())
		.build();
	}
	
	
	/**
     * This is for deleting the category from database
     * @param categoryId
     * */
	@Override
	public void deleteCategory(String categoryId) {
		Optional<Category> optional = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), categoryId);
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Category not found for this id: " + categoryId);
		}
		categoryRepository.delete(optional.get());
	}

}
