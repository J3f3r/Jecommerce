package com.jeferson.jecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeferson.jecommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
