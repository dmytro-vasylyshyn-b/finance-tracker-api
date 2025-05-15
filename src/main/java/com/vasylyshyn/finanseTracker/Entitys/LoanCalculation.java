package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_calculations")
public class LoanCalculation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Calculation calculation;

    private Double loanAmount;
    private Double interestRate;
    private Integer termMonths;
    private Double monthlyPayment;
    private Double totalPayment;
}
