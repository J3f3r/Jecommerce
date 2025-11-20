package com.jeferson.jecommerce.repositories;

import com.jeferson.jecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
