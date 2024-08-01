package com.expense.IO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequest {
	@NotBlank(message = "Expense Name must not be null")
	@Size(min = 3, message = "Expense Name must be atleast 3 characters")
	private String name;
	
	private String description;
	
	@NotNull(message = "Expense Amount should not be null")
	private BigDecimal amount;
	
	@NotBlank(message = "Category should not be null")
	private String categoryId;
	
	@NotNull(message = "Date should not be null")
	private Date date;
}
