package com.vasylyshyn.finanseTracker.Mappers;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Entitys.Expense;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.dtos.ExpenseRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.ExpenseResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponseDTO toDto(Expense expense) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setDate(expense.getDate());
        dto.setType(expense.getType());
        dto.setCategoryName(expense.getCategory().getName());
        return dto;
    }

    public static Expense fromDto(ExpenseRequestDTO dto, Category category, Users user) {
        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        expense.setType(dto.getType());
        expense.setCategory(category);
        expense.setUser(user);
        return expense;
    }
}
