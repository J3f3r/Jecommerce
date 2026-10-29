package com.jeferson.jecommerce.dto;

import com.jeferson.jecommerce.entities.OrderItem;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderItemDTO {

	@Schema(description = "Product ID", example = "1")
	private Long productId;
	
	@Schema(description = "Product name", example = "Smart TV")
	private String name;
	
	@Schema(description = "Product price", example = "2499.99")
	private Double price;
	
	@Schema(description = "Product quantity", example = "2")
	private Integer quantity;
	
	@Schema(description = "Product image URL", example = "https://raw.githubusercontent.com/devsuperior/apirest-resources/master/bds-ds.jpg")
	private String imgUrl;
	
	public OrderItemDTO() {}
	
	public OrderItemDTO(Long productId, String name, Double price, Integer quantity, String imgUrl) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.imgUrl = imgUrl;
	}
	
	public OrderItemDTO(OrderItem entity) {
		productId = entity.getProduct().getId();
		name = entity.getProduct().getName();
		price = entity.getPrice();
		quantity = entity.getQuantity();
		imgUrl = entity.getProduct().getImgUrl();
	}

	public Long getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getImgUrl() {
		return imgUrl;
	}
	
	public Double getSubTotal() {
		return price * quantity;
	}
}
