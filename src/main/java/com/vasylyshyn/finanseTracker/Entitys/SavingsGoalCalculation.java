package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "savings_goal_calculations")
public class SavingsGoalCalculation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Calculation calculation;

    private Double goalAmount;
    private Double currentSavings;
    private Double monthlyContribution;
    private Double annualReturnRate;
    private Integer monthsNeeded;
}