package com.jeferson.jecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeferson.jecommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
