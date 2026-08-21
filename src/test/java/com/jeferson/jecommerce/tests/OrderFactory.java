package com.jeferson.jecommerce.tests;

import java.time.Instant;

import com.jeferson.jecommerce.entities.Order;
import com.jeferson.jecommerce.entities.OrderItem;
import com.jeferson.jecommerce.entities.OrderStatus;
import com.jeferson.jecommerce.entities.Payment;
import com.jeferson.jecommerce.entities.Product;
import com.jeferson.jecommerce.entities.User;

public class OrderFactory {

	public static Order createOrder(User client) {
		
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
		
		Product product = ProductFactory.createProduct();
		
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		
		order.getItems().add(orderItem);
		
		return order;
	}
}
