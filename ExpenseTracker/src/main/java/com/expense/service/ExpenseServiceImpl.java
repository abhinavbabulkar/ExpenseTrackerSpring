package com.expense.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.expense.Dto.CategoryDTO;
import com.expense.Dto.ExpenseDto;
import com.expense.IO.ExpenseRequest;
import com.expense.exceptions.ResourceNotFoundException;
import com.expense.model.Category;
import com.expense.model.Expense;
import com.expense.model.User;
import com.expense.repository.CategoryRepository;
import com.expense.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
	
	private final ExpenseRepository expenseRepository;
	
	private final UserService userService;
	
	private final CategoryRepository categoryRepository;

	@Override
	public List<ExpenseDto> getAllExpenses(Pageable page) {
		List<Expense> listExpenses =  expenseRepository.findByUserId(userService.getLoggedInUser().getId(),page).toList();
		return listExpenses.stream().map(expense ->mapToDto(expense)).collect(Collectors.toList());
	}

	@Override
	public ExpenseDto getExpenseById(String expenseId) {
		Expense existignExpense = getExpense(expenseId);
		return mapToDto(existignExpense);
		
	}

	private Expense getExpense(String expenseId) {
		Optional<Expense> expense = expenseRepository.findByUserIdAndExpenseId(userService.getLoggedInUser().getId(), expenseId);
		if(!expense.isPresent()) {
			throw new ResourceNotFoundException("Expense is not found for the id " + expenseId);
		}
		return expense.get();
	}

	@Override
	public void deleteExpenseById(String expenseId) {
		
		Expense expense  = getExpense(expenseId);
		expenseRepository.delete(expense);
	}
	
	@Override
	public ExpenseDto saveExpenseDetails(ExpenseDto expenseDto) {
		//check the existence of category
		System.err.println("USer id: " + userService.getLoggedInUser().getId());
		Optional<Category> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDto.getCategoryId());
		if(!optionalCategory.isPresent()) {
			throw new ResourceNotFoundException("Category not found for the id " +expenseDto.getCategoryId());
		}
		expenseDto.setExpenseId(UUID.randomUUID().toString());
		
		//map to entity object
		Expense newExpense = mapToEntity(expenseDto);
		//save to the database
		newExpense.setCategory(optionalCategory.get());
		newExpense.setUser(userService.getLoggedInUser());
		newExpense = expenseRepository.save(newExpense);
		//map to response object
		return mapToDto(newExpense);
		
	}

	
	private Expense mapToEntity(ExpenseDto expenseDto) {
		return Expense.builder()
				.expenseId(expenseDto.getExpenseId())
				.name(expenseDto.getName())
				.description(expenseDto.getDescription())
				.date(expenseDto.getDate())
				.amount(expenseDto.getAmount())
				.build();
	}
	
	private ExpenseDto mapToDto(Expense newExpense) {
		return ExpenseDto.builder()
				.expenseId(newExpense.getExpenseId())
				.name(newExpense.getName())
				.description(newExpense.getDescription())
				.amount(newExpense.getAmount())
				.date(newExpense.getDate())
				.createdAt(newExpense.getCreatedAt())
				.updatedAt(newExpense.getUpdatedAt())
				.categoryDTO(mapToCategoryDTO(newExpense.getCategory()))
				.build();
	}


	private CategoryDTO mapToCategoryDTO(Category category) {
		return CategoryDTO.builder()
				.name(category.getName())
				.categoryId(category.getCategoryId())
				.build();
	}

	@Override
	public ExpenseDto updateExpenseDetails(String expenseId, ExpenseDto expenseDto) {
		Expense existingExpense  = getExpense(expenseId);
		if(expenseDto.getCategoryId()!=null) {
			Optional<Category> categOptional = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDto.getCategoryId());
			if(!categOptional.isPresent()) {
				throw new ResourceNotFoundException("Category not found for the id " +expenseDto.getCategoryId());
			}
			
			existingExpense.setCategory(categOptional.get());
			
		}
		
		existingExpense.setName(expenseDto.getName()!=null ? expenseDto.getName() : existingExpense.getName());
		existingExpense.setAmount(expenseDto.getAmount()!=null ? expenseDto.getAmount() : existingExpense.getAmount());
		existingExpense.setDescription(expenseDto.getDescription()!=null ? expenseDto.getDescription() : existingExpense.getDescription());
		existingExpense.setDate(expenseDto.getDate()!=null ? expenseDto.getDate() : existingExpense.getDate());
		existingExpense = expenseRepository.save(existingExpense);
		return mapToDto(existingExpense);
		
	}

	@Override
	public List<ExpenseDto> readByCategory(String category, Pageable page) {
		Optional<Category> optional = categoryRepository.findByNameAndUserId(category, userService.getLoggedInUser().getId());
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Category not found for the  name " +category);
			
		}
		
		List<Expense> expenseList = expenseRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId() ,optional.get().getId(), page).toList();
		return expenseList.stream().map(expense->mapToDto(expense)).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseDto> readByName(String keyword, Pageable page) {
		List<Expense> expenseList = expenseRepository.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(), keyword, page).toList();
		return expenseList.stream().map(expense->mapToDto(expense)).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseDto> readByDate(Date startDate, Date endDate, Pageable page) {
		if(startDate == null) {
			startDate = new Date(0);
			System.out.println(startDate);
		}
		if(endDate == null) {
			endDate = new Date(System.currentTimeMillis());
		}
		
		List<Expense> expenseList = expenseRepository.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(), startDate, endDate, page).toList();
		return expenseList.stream().map(expense->mapToDto(expense)).collect(Collectors.toList());
		
	}

}
