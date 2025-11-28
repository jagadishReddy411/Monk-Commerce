package com.mock.commerce.coupon.management.dto;

import java.util.List;

import lombok.Data;

@Data
public class Cart {
    private List<CartItem> items;
}
