package com.mock.commerce.coupon.management.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;
@Data
public class CartItem {
	@JsonAlias({"product_id", "productId"})
    private Long productId;
    private int quantity;
    private double price;
	}

