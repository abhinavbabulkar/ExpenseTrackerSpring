package com.expense.service;

import java.sql.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.expense.Dto.ExpenseDto;
import com.expense.IO.ExpenseRequest;
import com.expense.model.Expense;

public interface ExpenseService {
	
	List<ExpenseDto> getAllExpenses(Pageable page);
	ExpenseDto getExpenseById(String expenseId);
	void deleteExpenseById(String expenseId);
	ExpenseDto saveExpenseDetails(ExpenseDto expenseDto);
	ExpenseDto updateExpenseDetails(String expenseId, ExpenseDto expenseDto);
	
	List<ExpenseDto> readByCategory(String category, Pageable page);
	
	List<ExpenseDto> readByName(String keyword, Pageable page);
	
	List<ExpenseDto> readByDate(Date startDate, Date endDate, Pageable page);
	
}
