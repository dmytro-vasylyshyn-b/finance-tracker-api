package com.vasylyshyn.finanseTracker.Services;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import com.vasylyshyn.finanseTracker.Mappers.CategoryMapper;
import com.vasylyshyn.finanseTracker.Repositorys.CategoryRepository;
import com.vasylyshyn.finanseTracker.Repositorys.UserRepository;
import com.vasylyshyn.finanseTracker.dtos.CategoryRequestDTO;
import com.vasylyshyn.finanseTracker.dtos.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponseDTO create(CategoryRequestDTO dto, Authentication auth) {
        Users user = getUserFromAuth(auth);

        if (categoryRepository.existsByNameAndUserAndType(dto.getName(), user, dto.getType())) {
            throw new IllegalArgumentException("Category already exists");
        }

        Category category = CategoryMapper.fromDto(dto, user);

        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    public List<CategoryResponseDTO> getUserCategories(Authentication auth, MoneyType type) {
        Users user = getUserFromAuth(auth);

        List<Category> categories = (type == null)
                ? categoryRepository.findByUser(user)
                : categoryRepository.findByUserAndType(user, type);

        return categories.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto, Authentication auth) {
        Users user = getUserFromAuth(auth);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your category");
        }

        category.setName(dto.getName());
        category.setType(dto.getType());

        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    public void delete(Long id, Authentication auth) {
        Users user = getUserFromAuth(auth);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your category");
        }

        categoryRepository.delete(category);
    }

    private Users getUserFromAuth(Authentication auth) {
        Users userAuth = (Users) auth.getPrincipal();
        return userRepository.findByEmail(userAuth.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
