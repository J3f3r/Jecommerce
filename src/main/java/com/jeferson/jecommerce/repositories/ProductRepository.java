package com.jeferson.jecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // IMPORT CORRETO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jeferson.jecommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("SELECT obj FROM Product obj " +
	           "WHERE LOWER(obj.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	    Page<Product> searchByName(@Param("name")String name, Pageable pageable);
	// o mesmo motivo de ter usado param no user repository para resolver problemas na homologcao
}
