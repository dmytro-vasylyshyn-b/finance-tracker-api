package com.vasylyshyn.finanseTracker.Controllers;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Repositorys.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
}
