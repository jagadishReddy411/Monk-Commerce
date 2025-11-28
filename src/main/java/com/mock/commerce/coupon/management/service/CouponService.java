package com.mock.commerce.coupon.management.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mock.commerce.coupon.management.dto.CartRequest;
import com.mock.commerce.coupon.management.entity.Coupon;

public interface CouponService {

    Coupon createCoupon(Coupon coupon);

    List<Coupon> getAllCoupons();

    Optional<Coupon> getCoupon(Long id);

    Coupon updateCoupon(Long id, Coupon updated);

    Long deleteCoupon(Long id);

    public List<Map<String, Object>> getApplicableCoupons(CartRequest cart);
    
    public Map<String, Object> applyCoupon(Long couponId, CartRequest cartRequest);
}
