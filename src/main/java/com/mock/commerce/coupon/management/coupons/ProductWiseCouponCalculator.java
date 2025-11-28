package com.mock.commerce.coupon.management.coupons;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.mock.commerce.coupon.management.dto.Cart;
import com.mock.commerce.coupon.management.dto.CartItem;
import com.mock.commerce.coupon.management.entity.Coupon;

@Component
public class ProductWiseCouponCalculator implements CouponCalculator {

    @Override
    public double calculateDiscount(Coupon coupon, Cart cart) {
        JSONObject details = new JSONObject(coupon.getDetails());
        Long productId = details.getLong("product_id");
        double discountPercent = details.getDouble("discount");

        return cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .mapToDouble(i -> i.getPrice() * i.getQuantity() * discountPercent / 100)
                .sum();
    }

    @Override
    public double calculateItemDiscount(Coupon coupon, CartItem item, Cart cart) {
        JSONObject details = new JSONObject(coupon.getDetails());
        Long productId = details.getLong("product_id");
        double discountPercent = details.getDouble("discount");

        if (item.getProductId().equals(productId)) {
            return item.getPrice() * item.getQuantity() * discountPercent / 100;
        }
        return 0;
    }
}
