package com.mock.commerce.coupon.management.coupons;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.mock.commerce.coupon.management.dto.Cart;
import com.mock.commerce.coupon.management.dto.CartItem;
import com.mock.commerce.coupon.management.entity.Coupon;

@Component
public class BxGyCouponCalculator implements CouponCalculator {

    @Override
    public double calculateDiscount(Coupon coupon, Cart cart) {
        JSONObject details = new JSONObject(coupon.getDetails());
        JSONArray buyProducts = details.getJSONArray("buy_products");
        JSONArray getProducts = details.getJSONArray("get_products");
        int repetitionLimit = details.getInt("repition_limit");

        int buyCount = cart.getItems().stream()
                .filter(i -> containsProduct(buyProducts, i.getProductId()))
                .mapToInt(CartItem::getQuantity)
                .sum();

        int eligibleRepetitions = Math.min(buyCount / 2, repetitionLimit);

        double discount = 0;
        for (int i = 0; i < eligibleRepetitions; i++) {
            for (int j = 0; j < getProducts.length(); j++) {
                JSONObject gp = getProducts.getJSONObject(j);
                Long productId = gp.getLong("product_id");
                int qty = gp.getInt("quantity");

                discount += cart.getItems().stream()
                        .filter(ci -> ci.getProductId().equals(productId))
                        .mapToDouble(ci -> ci.getPrice() * qty)
                        .sum();
            }
        }
        return discount;
    }

    @Override
    public double calculateItemDiscount(Coupon coupon, CartItem item, Cart cart) {
        JSONObject details = new JSONObject(coupon.getDetails());
        JSONArray buyProducts = details.getJSONArray("buy_products");
        JSONArray getProducts = details.getJSONArray("get_products");
        int repetitionLimit = details.getInt("repition_limit");

        int buyCount = cart.getItems().stream()
                .filter(i -> containsProduct(buyProducts, i.getProductId()))
                .mapToInt(CartItem::getQuantity)
                .sum();

        int eligibleRepetitions = Math.min(buyCount / 2, repetitionLimit);

        double discount = 0;
        for (int rep = 0; rep < eligibleRepetitions; rep++) {
            for (int j = 0; j < getProducts.length(); j++) {
                JSONObject gp = getProducts.getJSONObject(j);
                long getProductId = gp.getLong("product_id");
                int freeQty = gp.getInt("quantity");
                if (item.getProductId().equals(getProductId)) {
                    discount += freeQty * item.getPrice();
                }
            }
        }
        return discount;
    }

    private boolean containsProduct(JSONArray array, Long productId) {
        for (int i = 0; i < array.length(); i++) {
            if (productId.equals(array.getJSONObject(i).getLong("product_id"))) {
                return true;
            }
        }
        return false;
    }
}
