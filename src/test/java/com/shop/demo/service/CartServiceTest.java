package com.shop.demo.service;

import com.shop.demo.constant.ItemSellStatus;
import com.shop.demo.dto.CartItemDto;
import com.shop.demo.entity.CartItem;
import com.shop.demo.entity.Item;
import com.shop.demo.entity.Member;
import com.shop.demo.repository.CartItemRepository;
import com.shop.demo.repository.ItemRepository;
import com.shop.demo.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CartServiceTest {

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  CartService cartService;

  @Autowired
  CartItemRepository cartItemRepository;

  @Test
  @DisplayName("장바구니 담기 테스트")
  void addCart(){
    Item item = saveItem();
    Member member = saveMember();

    CartItemDto cartItemDto = new CartItemDto();
    cartItemDto.setCount(5);
    cartItemDto.setItemId(item.getId());

    Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(EntityNotFoundException::new);

    assertEquals(item.getId(), cartItem.getItem().getId());
    assertEquals(cartItemDto.getCount(), cartItem.getCount());
  }

  Item saveItem(){
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("테스트 상품 상세 설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);

    return itemRepository.save(item);
  }

  Member saveMember(){
    Member member = new Member();
    member.setEmail("test@test.com");

    return memberRepository.save(member);
  }

}