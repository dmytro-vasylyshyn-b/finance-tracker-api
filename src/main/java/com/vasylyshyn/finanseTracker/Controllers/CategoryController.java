package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import com.vasylyshyn.finanseTracker.Mappers.CategoryMapper;
import com.vasylyshyn.finanseTracker.Repositorys.CategoryRepository;
import com.vasylyshyn.finanseTracker.Repositorys.UserRepository;
import com.vasylyshyn.finanseTracker.dtos.CategoryRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.CategoryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public CategoryResponseDTO create(@RequestBody CategoryRequestDTO dto,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (categoryRepository.existsByNameAndUserAndType(dto.getName(), user, dto.getType())) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setType(dto.getType());
        category.setUser(user);

        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @GetMapping
    public List<CategoryResponseDTO> getUserCategories(@RequestParam(required = false) MoneyType type,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Category> categories = (type == null)
                ? categoryRepository.findByUser(user)
                : categoryRepository.findByUserAndType(user, type);

        return categories.stream().map(CategoryMapper::toDto).toList();
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable Long id,
                                      @RequestBody CategoryRequestDTO dto,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your category");
        }

        category.setName(dto.getName());
        category.setType(dto.getType());

        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal UserDetails userDetails) {
        Users user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your category");
        }

        categoryRepository.delete(category);
    }
}
