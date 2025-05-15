package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "investment_calculations")
public class InvestmentCalculation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Calculation calculation;

    private Double initialAmount;
    private Double annualReturnRate;
    private Integer years;
    private Double futureValue;
}
