package com.mock.commerce.coupon.management.controller;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mock.commerce.coupon.management.dto.CartRequest;
import com.mock.commerce.coupon.management.entity.Coupon;
import com.mock.commerce.coupon.management.exception.CouponNotFoundException;
import com.mock.commerce.coupon.management.service.CouponService;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;
    
    @Autowired
    private Environment env;
    @PostMapping
    public ResponseEntity<String> createCoupon(@RequestBody Coupon coupon) {
        Coupon coup= couponService.createCoupon(coupon);
        String pmsg=env.getProperty("COUPON_CREATED_SUCCESSFULLY");
        String msg=pmsg+"::"+coup.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCoupon(@PathVariable Long id) {
        Coupon coupon = couponService.getCoupon(id)
                .orElseThrow(() -> new CouponNotFoundException(id));
        return ResponseEntity.ok(coupon);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
        Coupon updatedCoupon = couponService.updateCoupon(id, coupon);
        return ResponseEntity.ok(updatedCoupon);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long id) {
        Long cId=couponService.deleteCoupon(id);
        String envmsg = env.getProperty("COUPON_DELETED_SUCCESSFULLY");
        String msg=envmsg+":"+cId;
        return ResponseEntity.ok(msg);
    }


    @PostMapping("/applicable-coupons")
    public ResponseEntity<Map<String, Object>> getApplicableCoupons(@RequestBody CartRequest cart) {
    	 List<Map<String, Object>> applicableCoupons = couponService.getApplicableCoupons(cart);
         return ResponseEntity.ok(Map.of("applicable_coupons", applicableCoupons));

    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Map<String, Object>> applyCoupon(
            @PathVariable Long id,
            @RequestBody CartRequest cartRequest) {

        Map<String, Object> updatedCart = couponService.applyCoupon(id, cartRequest);

        return ResponseEntity.ok(Map.of("updated_cart", updatedCart));
    }

}