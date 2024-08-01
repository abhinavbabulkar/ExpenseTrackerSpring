package com.expense.Dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.expense.IO.CategoryResponse;
import com.expense.IO.ExpenseResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {
	private String expenseId;
	private String name;
	private String description;
	private BigDecimal amount;
	private Date date;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private CategoryDTO categoryDTO;
	private String categoryId;
	private UserDTO userDTO;
}
