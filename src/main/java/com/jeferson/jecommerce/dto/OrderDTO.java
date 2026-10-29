package com.jeferson.jecommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.jeferson.jecommerce.entities.Order;
import com.jeferson.jecommerce.entities.OrderItem;
import com.jeferson.jecommerce.entities.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {

	@Schema(description = "Order ID", example = "1")
	private Long id;
	
	@Schema(description = "Order moment", example = "2023-07-13T20:50:00Z")
	private Instant moment;
	
	@Schema(description = "Order status", example = "WAITING_PAYMENT")
	private OrderStatus status;
	
	@Schema(description = "Order client")
	private ClientDTO client;
	
	@Schema(description = "Order payment details")
	private PaymentDTO payment;
	
	@Schema(description = "Order items")
	@NotEmpty(message = "Deve ter pelo menos um item")
	private List<OrderItemDTO> items = new ArrayList<>();

	public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client, PaymentDTO payment,
			List<OrderItemDTO> items) {

		this.id = id;
		this.moment = moment;
		this.status = status;
		this.client = client;
		this.payment = payment;
		this.items = items;
	}
	
	public OrderDTO() {}
	
	public OrderDTO(Order entity) {
		id = entity.getId();
		moment = entity.getMoment();
		status = entity.getStatus();
		client = new ClientDTO(entity.getClient());
		payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
		
		for (OrderItem item : entity.getItems()) {
			OrderItemDTO itemDto = new OrderItemDTO(item);
			items.add(itemDto);
		}
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public ClientDTO getClient() {
		return client;
	}

	public PaymentDTO getPayment() {
		return payment;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}
	
	public Double getTotal() {
		double sum = 0.0;
		
		for (OrderItemDTO item : items) {
			sum += item.getSubTotal();
		}
		return sum;
	}
}
