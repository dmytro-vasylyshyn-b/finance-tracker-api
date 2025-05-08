package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Entitys.Expense;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import com.vasylyshyn.finanseTracker.Mappers.ExpenseMapper;
import com.vasylyshyn.finanseTracker.Repositorys.CategoryRepository;
import com.vasylyshyn.finanseTracker.Repositorys.ExpenseRepository;
import com.vasylyshyn.finanseTracker.Repositorys.UserRepository;
import com.vasylyshyn.finanseTracker.dtos.ExpenseRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.ExpenseResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ExpenseResponseDTO create(@RequestBody ExpenseRequestDTO dto,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Category does not belong to this user");
        }

        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        expense.setType(dto.getType());
        expense.setCategory(category);
        expense.setUser(user);

        return ExpenseMapper.toDto(expenseRepository.save(expense));
    }

    @GetMapping
    public List<ExpenseResponseDTO> getUserExpenses(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseRepository.findAllByUser(user)
                .stream().map(ExpenseMapper::toDto).toList();
    }

    @GetMapping("/filtered")
    public Page<ExpenseResponseDTO> getFilteredExpenses(
            @RequestParam(required = false) MoneyType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date,desc") String[] sort,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Sort sortObj = Sort.by(Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                }).toList());

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Expense> result = expenseRepository.searchByFilters(user, type, fromDate, toDate, minAmount, maxAmount, pageable);

        return result.map(ExpenseMapper::toDto);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDTO updateExpense(@PathVariable Long id,
                                            @RequestBody ExpenseRequestDTO dto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your record");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        expense.setAmount(dto.getAmount());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        expense.setType(dto.getType());
        expense.setCategory(category);

        return ExpenseMapper.toDto(expenseRepository.save(expense));
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id,
                              @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your record");
        }

        expenseRepository.delete(expense);
    }

}

