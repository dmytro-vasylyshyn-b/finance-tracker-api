package com.vasylyshyn.finanseTracker.dtos;

import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequestDTO {
    private Double amount;
    private String description;
    private LocalDate date;
    private MoneyType type;
    private Long categoryId;
}
