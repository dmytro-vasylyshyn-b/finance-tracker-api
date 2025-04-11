package com.vasylyshyn.finanseTracker.Repositorys;

import com.vasylyshyn.finanseTracker.Entitys.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}

