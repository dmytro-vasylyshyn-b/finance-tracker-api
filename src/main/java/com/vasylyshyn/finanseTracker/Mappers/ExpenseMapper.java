package com.vasylyshyn.finanseTracker.Mappers;

import com.vasylyshyn.finanseTracker.Entitys.Expense;
import com.vasylyshyn.finanseTracker.dtos.ExpenseResponseDTO;

public class ExpenseMapper {
    public static ExpenseResponseDTO toDto(Expense expense) {
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDescription(expense.getDescription());
        dto.setDate(expense.getDate());
        dto.setType(expense.getType());
        dto.setCategoryName(expense.getCategory().getName());
        return dto;
    }
}
