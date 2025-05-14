package com.vasylyshyn.finanseTracker.Services;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Entitys.Expense;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import com.vasylyshyn.finanseTracker.Mappers.ExpenseMapper;
import com.vasylyshyn.finanseTracker.Repositorys.CategoryRepository;
import com.vasylyshyn.finanseTracker.Repositorys.ExpenseRepository;
import com.vasylyshyn.finanseTracker.dtos.ExpenseRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.ExpenseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;

    public List<ExpenseResponseDTO> getAllExpenses(Users user) {
        return expenseRepository.findAllByUser(user).stream()
                .map(expenseMapper::toDto)
                .collect(toList());
    }

    public Page<ExpenseResponseDTO> getFilteredExpenses(
            Users user,
            MoneyType type,
            LocalDate fromDate,
            LocalDate toDate,
            Double minAmount,
            Double maxAmount,
            Pageable pageable
    ) {
        Page<Expense> page = expenseRepository.searchByFilters(
                user,
                type,
                fromDate,
                toDate,
                minAmount,
                maxAmount,
                pageable
        );

        return page.map(expenseMapper::toDto);
    }

    public void addExpense(Users user, ExpenseRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Категорія не знайдена"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Ця категорія не належить користувачу");
        }

        Expense expense = ExpenseMapper.fromDto(dto, category, user);
        expenseRepository.save(expense);
    }

    public void updateExpense(Long id, ExpenseRequestDTO dto, Users user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Транзакція не знайдена"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Доступ заборонено");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Категорія не знайдена"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Ця категорія не належить користувачу");
        }

        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        expense.setType(dto.getType());
        expense.setCategory(category);

        expenseRepository.save(expense);
    }

    public void deleteExpense(Long id, Users user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Транзакція не знайдена"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Доступ заборонено");
        }

        expenseRepository.delete(expense);
    }
}
