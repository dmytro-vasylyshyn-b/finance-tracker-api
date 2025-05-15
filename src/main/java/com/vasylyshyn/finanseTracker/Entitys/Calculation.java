package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private CalculatorType type;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "calculation", cascade = CascadeType.ALL)
    private LoanCalculation loanCalculation;

    @OneToOne(mappedBy = "calculation", cascade = CascadeType.ALL)
    private InvestmentCalculation investmentCalculation;

    @OneToOne(mappedBy = "calculation", cascade = CascadeType.ALL)
    private EducationCalculation  educationCalculation ;

    @OneToOne(mappedBy = "calculation", cascade = CascadeType.ALL)
    private MortgageCalculation  mortgageCalculation;

    @OneToOne(mappedBy = "calculation", cascade = CascadeType.ALL)
    private SavingsGoalCalculation savingsGoalCalculation;

    @OneToOne(mappedBy = "calculation", cascade = CascadeType.ALL)
    private RetirementCalculation retirementCalculation;


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
