package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import com.vasylyshyn.finanseTracker.Services.CategoryService;
import com.vasylyshyn.finanseTracker.dtos.CategoryRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponseDTO create(@RequestBody CategoryRequestDTO dto, Authentication auth) {
        return categoryService.create(dto, auth);
    }

    @GetMapping
    public List<CategoryResponseDTO> getUserCategories(@RequestParam(required = false) MoneyType type,
                                                       Authentication auth) {
        return categoryService.getUserCategories(auth, type);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable Long id,
                                      @RequestBody CategoryRequestDTO dto,
                                      Authentication auth) {
        return categoryService.update(id, dto, auth);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        categoryService.delete(id, auth);
    }
}
