package com.shop.demo.service;

import com.shop.demo.dto.CartDetailDto;
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
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

  @Transactional(readOnly = true)
  public List<CartDetailDto> getCartList(String email){

    List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

    Member member = memberRepository.findByEmail(email);
    Cart cart = cartRepository.findByMemberId(member.getId());
    if(cart == null){
      return cartDetailDtoList;
    }

    cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
    return cartDetailDtoList;
  }

  @Transactional(readOnly = true)
  public boolean validateCartItem(Long cartItemId, String email){
    Member curMember = memberRepository.findByEmail(email);
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);
    Member savedMember = cartItem.getCart().getMember();

    if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
      return false;
    }

    return true;
  }

  public void updateCartItemCount(Long cartItemId, int count){
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);

    cartItem.updateCount(count);
  }

  public void deleteCartItem(Long cartItemId) {
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);
    cartItemRepository.delete(cartItem);
  }
}
