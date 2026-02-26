package com.jeferson.jecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeferson.jecommerce.entities.OrderItem;
import com.jeferson.jecommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}