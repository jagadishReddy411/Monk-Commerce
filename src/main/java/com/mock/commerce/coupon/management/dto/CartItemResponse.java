package com.mock.commerce.coupon.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartItemResponse {
    private Long product_id;
    private int quantity;
    private double price;
    private double total_discount;

   
}