package com.mock.commerce.coupon.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mock.commerce.coupon.management.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

 
}