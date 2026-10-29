package com.jeferson.jecommerce.dto;

import java.time.Instant;

import com.jeferson.jecommerce.entities.Payment;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentDTO {

	@Schema(description = "Payment ID", example = "1")
	private Long id;
	
	@Schema(description = "Payment moment", example = "2023-07-13T21:00:00Z")
	private Instant moment;
	
	public PaymentDTO() {}
	
	public PaymentDTO(Long id, Instant moment) {
		this.id = id;
		this.moment = moment;
	}
	
	public PaymentDTO(Payment entity) {
		id = entity.getId();
		moment = entity.getMoment();
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}
}
