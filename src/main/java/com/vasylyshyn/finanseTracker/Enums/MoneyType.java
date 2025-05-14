package com.vasylyshyn.finanseTracker.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MoneyType {
    EXPENSE, INCOME, INVESTMENTS;

    @JsonCreator
    public static MoneyType fromString(String value) {
        return MoneyType.valueOf(value.toUpperCase());
    }
}

