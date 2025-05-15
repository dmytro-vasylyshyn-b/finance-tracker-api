package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "retirement_calculations")
public class RetirementCalculation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Calculation calculation;

    private Double currentAge;
    private Double retirementAge;
    private Double currentSavings;
    private Double monthlyContribution;
    private Double annualReturnRate;
    private Double projectedSavings;
}
