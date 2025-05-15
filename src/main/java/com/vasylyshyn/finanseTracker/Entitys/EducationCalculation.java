package com.vasylyshyn.finanseTracker.Entitys;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "education_calculations")
public class EducationCalculation {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Calculation calculation;

    private Double currentSavings;
    private Double annualContribution;
    private Double annualCostGrowthRate;
    private Integer yearsUntilCollege;
    private Double estimatedCost;
}