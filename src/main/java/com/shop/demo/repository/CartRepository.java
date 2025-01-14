package com.shop.demo.repository;

import com.shop.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Cart findByMemberId(Long memberId);

}
