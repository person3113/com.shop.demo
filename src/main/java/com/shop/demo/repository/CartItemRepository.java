package com.shop.demo.repository;

import com.shop.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}
