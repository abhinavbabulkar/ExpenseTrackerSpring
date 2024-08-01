package com.expense.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.expense.model.Category;
import com.expense.model.Expense;
import com.expense.model.LoginModel;

import java.util.List;
import java.util.Optional;
import java.util.Date;



@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
	//select * from tbl_expenses where Category=?
	Page<Expense> findByUserIdAndCategory(Long userId, String category, Pageable page);
	
	Page<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable page);
	
	//select * from tbl_expenses where name like '%keyword%'
	Page<Expense> findByUserIdAndNameContaining(Long userId, String keyword, Pageable page);
	
	//select * from tbl_expenses where date BETWEEN 'startDate' and 'endDate'
	Page<Expense> findByUserIdAndDateBetween(Long userId, Date startdate, Date endDate, Pageable page);
	
	Page<Expense> findByUserId(Long userId, Pageable page );
	
	Optional<Expense> findByUserIdAndExpenseId(Long userId, String expenseId);
	
	
}
