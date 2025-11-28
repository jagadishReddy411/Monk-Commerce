package com.mock.commerce.coupon.management.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mock.commerce.coupon.management.coupons.CouponEngine;
import com.mock.commerce.coupon.management.dto.CartItem;
import com.mock.commerce.coupon.management.dto.CartRequest;
import com.mock.commerce.coupon.management.entity.Coupon;
import com.mock.commerce.coupon.management.enums.CouponType;
import com.mock.commerce.coupon.management.exception.CouponNotFoundException;
import com.mock.commerce.coupon.management.repository.CouponRepository;

@Service
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponEngine couponEngine;

    @Autowired
    public CouponServiceImpl(CouponRepository couponRepository, CouponEngine couponEngine) {
        this.couponRepository = couponRepository;
        this.couponEngine = couponEngine;
    }

    @Override
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> getCoupon(Long id) {
        return couponRepository.findById(id);
    }

    @Override
    public Coupon updateCoupon(Long id, Coupon updated) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
        coupon.setType(updated.getType());
        coupon.setDetails(updated.getDetails());
        return couponRepository.save(coupon);
    }

    @Override
    public Long deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        couponRepository.delete(coupon);
        return coupon.getId();
    }

    @Override
    public List<Map<String, Object>> getApplicableCoupons(CartRequest cart)  {
    	 return couponRepository.findAll().stream()
                 .map(coupon -> {
                     double discount = couponEngine.calculateDiscount(coupon, cart.getCart());
                     Map<String, Object> map = new HashMap<>();
                     map.put("coupon_id", coupon.getId());
                     map.put("type", coupon.getType());
                     map.put("discount", discount);
                     return map;
                 })
                 .filter(map -> (double) map.get("discount") > 0)
                 .collect(Collectors.toList());
    }
    @Override
    public Map<String, Object> applyCoupon(Long couponId, CartRequest cartRequest) {
        // Fetch coupon
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));

        List<Map<String, Object>> updatedItems = new ArrayList<>();
        double totalPrice = 0;
        double totalDiscount = 0;

        for (CartItem item : cartRequest.getCart().getItems()) {
            double itemTotal = item.getPrice() * item.getQuantity();
            double itemDiscount = couponEngine.calculateItemDiscount(coupon, item, cartRequest.getCart());

            totalPrice += itemTotal;
            totalDiscount += itemDiscount;
            
            if(coupon.getType() == CouponType.BXGY) {
            	totalPrice=totalPrice+totalDiscount;
            }
            updatedItems.add(Map.of(
                    "product_id", item.getProductId(),
                    "quantity", item.getQuantity() + (
                            (coupon.getType() == CouponType.BXGY && itemDiscount > 0)
                                    ? (int) (itemDiscount / item.getPrice())
                                    : 0
                    ),
                    "price", item.getPrice(),
                    "total_discount", itemDiscount
            ));
        }

        return Map.of(
                "items", updatedItems,
                "total_price", totalPrice,
                "final_total_discount", totalDiscount,
                "final_price", totalPrice - totalDiscount
        );
    }
  
}