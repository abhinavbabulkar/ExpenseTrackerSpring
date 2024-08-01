package com.expense.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.expense.Dto.CategoryDTO;
import com.expense.IO.CategoryRequest;
import com.expense.IO.CategoryResponse;
import com.expense.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService;
	
	
	/**
     * API for creating the category
     * @param categoryRequest
     * @return CategoryResponse
     * */
	@PostMapping
	public CategoryResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
	   CategoryDTO categoryDTO = maptoDto(categoryRequest);
	   categoryDTO = categoryService.saveCategory(categoryDTO);
	   return mapToResponse(categoryDTO);
	}
	
	
	/**
     * API for reading the categories
     * @return list
     * */
	@GetMapping
	public List<CategoryResponse> readCategories(){
		List<CategoryDTO> categoryDTOs = categoryService.getAllCategories();
		return categoryDTOs.stream().map(categoryDTO->mapToResponse(categoryDTO)).collect(Collectors.toList());
	}
	
	
	/**
     * API for deleting the category
     * @param categoryId
     *
     * */
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{categoryId}")
	public void deleteCategory(@PathVariable String categoryId) {
		categoryService.deleteCategory(categoryId);
	}

	
	/**
     * Mapper method for converting DTO object to Response object
     * @param categoryDTO
     * @return CategoryResponse
     * */
	private CategoryResponse mapToResponse(CategoryDTO categoryDTO) {
		return CategoryResponse.builder()
				.categoryId(categoryDTO.getCategoryId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .categoryIcon(categoryDTO.getCategoryIcon())
                .createdAt(categoryDTO.getCreatedAt())
                .updatedAt(categoryDTO.getUpdatedAt())
                .build();
	}

	
	/**
     * Mapper method for converting Request object to DTO object
     * @param categoryRequest
     * @return CategoryDTO
     * */
	private CategoryDTO maptoDto(CategoryRequest categoryRequest) {
		return CategoryDTO.builder()
				.name(categoryRequest.getName())
				.description(categoryRequest.getDescription())
				.categoryIcon(categoryRequest.getIcon())
				.build();
	}
}
