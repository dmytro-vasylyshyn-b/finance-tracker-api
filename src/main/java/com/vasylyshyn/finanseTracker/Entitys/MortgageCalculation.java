package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mortgage_calculations")
public class MortgageCalculation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Calculation calculation;

    private Double propertyPrice;
    private Double downPayment;
    private Double loanAmount;
    private Double interestRate;
    private Integer loanTermYears;
    private Double monthlyPayment;
    private Double totalPayment;
}