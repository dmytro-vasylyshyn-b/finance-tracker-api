package com.vasylyshyn.finanseTracker.Repositorys;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.Entitys.Users;
import com.vasylyshyn.finanseTracker.Enums.MoneyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserAndType(Users user, MoneyType type);
    boolean existsByNameAndUserAndType(String name, Users user, MoneyType type);
    List<Category> findByUser(Users user);
}
