package com.vasylyshyn.finanseTracker.Repositorys;

import com.vasylyshyn.finanseTracker.Entitys.Expense;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUser(Users user);
    @Query("SELECT e FROM Expense e WHERE e.user = :user "
            + "AND (:type IS NULL OR e.type = :type) "
            + "AND e.date BETWEEN :fromDate AND :toDate "
            + "AND (:minAmount IS NULL OR e.amount >= :minAmount) "
            + "AND (:maxAmount IS NULL OR e.amount <= :maxAmount)")
    Page<Expense> searchByFilters(
            @Param("user") Users user,
            @Param("type") MoneyType type,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            Pageable pageable
    );

}


