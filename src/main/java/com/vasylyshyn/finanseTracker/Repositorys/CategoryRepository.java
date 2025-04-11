package com.vasylyshyn.finanseTracker.Repositorys;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}

