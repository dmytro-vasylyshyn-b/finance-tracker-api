package com.vasylyshyn.finanseTracker.Mappers;

import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseFilterDTO {
    private MoneyType type;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double minAmount;
    private Double maxAmount;
}
