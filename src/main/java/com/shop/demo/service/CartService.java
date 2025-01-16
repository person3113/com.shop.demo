package com.shop.demo.service;

import com.shop.demo.dto.CartItemDto;
import com.shop.demo.entity.Cart;
import com.shop.demo.entity.CartItem;
import com.shop.demo.entity.Item;
import com.shop.demo.entity.Member;
import com.shop.demo.repository.CartItemRepository;
import com.shop.demo.repository.CartRepository;
import com.shop.demo.repository.ItemRepository;
import com.shop.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;

  public Long addCart(CartItemDto cartItemDto, String email){

    Item item = itemRepository.findById(cartItemDto.getItemId())
        .orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(email);

    Cart cart = cartRepository.findByMemberId(member.getId());
    if(cart == null){
      cart = Cart.createCart(member);
      cartRepository.save(cart);
    }

    CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

    if(savedCartItem != null){
      savedCartItem.addCount(cartItemDto.getCount());
      return savedCartItem.getId();
    } else {
      CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
      cartItemRepository.save(cartItem);
      return cartItem.getId();
    }
  }

}
