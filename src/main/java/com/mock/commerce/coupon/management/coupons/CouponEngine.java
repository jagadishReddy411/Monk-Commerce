package com.mock.commerce.coupon.management.coupons;

import org.springframework.stereotype.Component;
import com.mock.commerce.coupon.management.dto.Cart;
import com.mock.commerce.coupon.management.dto.CartItem;
import com.mock.commerce.coupon.management.entity.Coupon;

@Component
public class CouponEngine {

    private final CartWiseCouponCalculator cartWiseCalculator;
    private final ProductWiseCouponCalculator productWiseCalculator;
    private final BxGyCouponCalculator bxGyCalculator;

    public CouponEngine(CartWiseCouponCalculator cartWiseCalculator,
                        ProductWiseCouponCalculator productWiseCalculator,
                        BxGyCouponCalculator bxGyCalculator) {
        this.cartWiseCalculator = cartWiseCalculator;
        this.productWiseCalculator = productWiseCalculator;
        this.bxGyCalculator = bxGyCalculator;
    }

    public double calculateDiscount(Coupon coupon, Cart cart) {
        switch (coupon.getType()) {
            case CART_WISE: return cartWiseCalculator.calculateDiscount(coupon, cart);
            case PRODUCT_WISE: return productWiseCalculator.calculateDiscount(coupon, cart);
            case BXGY: return bxGyCalculator.calculateDiscount(coupon, cart);
            default: return 0;
        }
    }

    public double calculateItemDiscount(Coupon coupon, CartItem item, Cart cart) {
        switch (coupon.getType()) {
            case CART_WISE: return cartWiseCalculator.calculateItemDiscount(coupon, item, cart);
            case PRODUCT_WISE: return productWiseCalculator.calculateItemDiscount(coupon, item, cart);
            case BXGY: return bxGyCalculator.calculateItemDiscount(coupon, item, cart);
            default: return 0;
        }
    }
}
