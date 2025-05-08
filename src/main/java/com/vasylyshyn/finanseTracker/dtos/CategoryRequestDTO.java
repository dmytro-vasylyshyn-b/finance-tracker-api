package com.vasylyshyn.finanseTracker.dtos;

import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;
    private MoneyType type;
}
