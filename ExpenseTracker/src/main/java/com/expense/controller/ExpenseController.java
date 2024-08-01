package com.expense.controller;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.expense.Dto.CategoryDTO;
import com.expense.Dto.ExpenseDto;
import com.expense.IO.CategoryResponse;
import com.expense.IO.ExpenseRequest;
import com.expense.IO.ExpenseResponse;
import com.expense.model.Expense;
import com.expense.service.ExpenseService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/expenses")
public class ExpenseController {
	
	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping("/list")
	public String getList() {
		return "This is Expense List";
	}
	
	//Pagination url - /expenses?page=0&size=2&sort=amount,desc
	@GetMapping
	public List<ExpenseResponse> getAllExpenses(Pageable page) {
		List<ExpenseDto> listExpense = expenseService.getAllExpenses(page);
		return listExpense.stream().map(expenseDto -> mapToResponse(expenseDto)).collect(Collectors.toList());
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping
	public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest) {
		ExpenseDto expenseDto = maptoDto(expenseRequest);
		expenseDto = expenseService.saveExpenseDetails(expenseDto);
		return mapToResponse(expenseDto);
	}
	
	
	private ExpenseResponse mapToResponse(ExpenseDto expenseDto) {
		return ExpenseResponse.builder()
				.expenseId(expenseDto.getExpenseId())
				.name(expenseDto.getName())
				.description(expenseDto.getDescription())
				.amount(expenseDto.getAmount())
				.date(expenseDto.getDate())
				.category(mapToCategoryResponse(expenseDto.getCategoryDTO()))
				.createdAt(expenseDto.getCreatedAt())
				.updatedAt(expenseDto.getUpdatedAt())
				.build();
	}

	private CategoryResponse mapToCategoryResponse(CategoryDTO categoryDTO) {
		return CategoryResponse.builder()
				.categoryId(categoryDTO.getCategoryId())
				.name(categoryDTO.getName())
				.build();
	}

	private ExpenseDto maptoDto(@Valid ExpenseRequest expenseRequest) {
		return ExpenseDto.builder()
				.name(expenseRequest.getName())
				.description(expenseRequest.getDescription())
				.amount(expenseRequest.getAmount())
				.date(expenseRequest.getDate())
				.categoryId(expenseRequest.getCategoryId())
				.build();
	}

	//using PathVariable for getting parameter from URL
	@GetMapping("/{expenseId}")
	public ExpenseResponse getExpenseById(@PathVariable("expenseId") String expenseId) {
		ExpenseDto expenseDto = expenseService.getExpenseById(expenseId);
		return mapToResponse(expenseDto);
	}
	
	//using @REquestParam for getting parameter from URL
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping
	public String deleteExpenseById(@RequestParam String expenseId) {
		expenseService.deleteExpenseById(expenseId);
		return "Delete Expense ID is: " + expenseId;
	}
	
	
	@PutMapping("/{expenseId}")
	public ExpenseResponse updateExpenseDetails(@PathVariable("expenseId") String expenseId,@RequestBody ExpenseRequest expenseRequest) {
		ExpenseDto updatedExpenseDto = maptoDto(expenseRequest);
		updatedExpenseDto = expenseService.updateExpenseDetails(expenseId, updatedExpenseDto);
		return mapToResponse(updatedExpenseDto);
		
	}
	
	@GetMapping("/category")
	public List<ExpenseResponse> getExpenseByCategory(@RequestParam String category, Pageable page){
		List<ExpenseDto> list = expenseService.readByCategory(category, page);
		return list.stream().map(expenseRes->mapToResponse(expenseRes)).collect(Collectors.toList());
		
	}
	
	@GetMapping("/name")
	public List<ExpenseResponse> getAllExpenseByName(@RequestParam("keyword") String keyword, Pageable page){
		List<ExpenseDto> list = expenseService.readByName(keyword, page);
		return list.stream().map(expenseRes->mapToResponse(expenseRes)).collect(Collectors.toList());
	}
	
	@GetMapping("/date")
	public List<ExpenseResponse> getAllExpensesByDate(@RequestParam(required = false) Date startDate,
			@RequestParam(required = false) Date endDate, Pageable page){
		List<ExpenseDto> list = expenseService.readByDate(startDate, endDate, page);
		return list.stream().map(expenseRes->mapToResponse(expenseRes)).collect(Collectors.toList());
		
	}
	
	
}
