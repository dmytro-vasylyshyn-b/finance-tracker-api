package com.vasylyshyn.finanseTracker.Repositorys;

import com.vasylyshyn.finanseTracker.Entitys.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {

}
