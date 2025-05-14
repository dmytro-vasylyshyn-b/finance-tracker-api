package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import com.vasylyshyn.finanseTracker.Repositorys.UserRepository;
import com.vasylyshyn.finanseTracker.Services.ExpenseService;
import com.vasylyshyn.finanseTracker.dtos.ExpenseRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.ExpenseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import java.util.List;


@RestController
@RequestMapping("/api/expenses")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses(Authentication auth) {
        Users user = (Users) auth.getPrincipal();
        return ResponseEntity.ok(expenseService.getAllExpenses(user));
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<ExpenseResponseDTO>> getFilteredExpenses(
            @RequestParam(required = false) MoneyType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth
    ) {
        Users userAuth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<ExpenseResponseDTO> result = expenseService.getFilteredExpenses(user, type, fromDate, toDate, minAmount, maxAmount, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Void> addExpense(@RequestBody ExpenseRequestDTO expenseDto, Authentication auth) {
        Users userAuth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        expenseService.addExpense(user, expenseDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDTO dto, Authentication auth) {
        Users userAuth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        expenseService.updateExpense(id, dto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Authentication auth) {
        Users userAuth = (Users) auth.getPrincipal();
        Users user = userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        expenseService.deleteExpense(id, user);
        return ResponseEntity.noContent().build();
    }
}

