package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Entitys.Income;
import com.vasylyshyn.finanseTracker.Repositorys.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/income")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    @GetMapping
    public List<Income> getIncomes() {
        return incomeRepository.findAll();
    }

    @PostMapping
    public Income addIncome(@RequestBody Income income) {
        return incomeRepository.save(income);
    }
}
