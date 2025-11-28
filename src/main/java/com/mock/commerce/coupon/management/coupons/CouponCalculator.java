package com.mock.commerce.coupon.management.coupons;

import com.mock.commerce.coupon.management.dto.Cart;
import com.mock.commerce.coupon.management.dto.CartItem;
import com.mock.commerce.coupon.management.entity.Coupon;

public interface CouponCalculator {

    double calculateDiscount(Coupon coupon, Cart cart);

    double calculateItemDiscount(Coupon coupon, CartItem item, Cart cart);

}
