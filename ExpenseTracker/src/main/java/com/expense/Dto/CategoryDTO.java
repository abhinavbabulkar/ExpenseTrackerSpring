package com.expense.Dto;

import java.sql.Timestamp;

import com.expense.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
	
	private String categoryId;
    private String name;
    private String description;
    private String categoryIcon;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserDTO user;
}
