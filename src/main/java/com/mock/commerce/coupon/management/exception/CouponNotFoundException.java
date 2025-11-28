package com.mock.commerce.coupon.management.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(Long id) {
        super("Coupon not found with id: " + id);
    }
}
