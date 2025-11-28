package com.mock.commerce.coupon.management.coupons;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.mock.commerce.coupon.management.dto.Cart;
import com.mock.commerce.coupon.management.dto.CartItem;
import com.mock.commerce.coupon.management.entity.Coupon;

@Component
public class CartWiseCouponCalculator implements CouponCalculator {

    @Override
    public double calculateDiscount(Coupon coupon, Cart cart) {
        JSONObject details = new JSONObject(coupon.getDetails());
        double threshold = details.getDouble("threshold");
        double discountPercent = details.getDouble("discount");

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        return total >= threshold ? total * discountPercent / 100 : 0;
    }

    @Override
    public double calculateItemDiscount(Coupon coupon, CartItem item, Cart cart) {
        JSONObject details = new JSONObject(coupon.getDetails());
        double threshold = details.getDouble("threshold");
        double discountPercent = details.getDouble("discount");

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        return total >= threshold ? item.getPrice() * item.getQuantity() * discountPercent / 100 : 0;
    }
}
