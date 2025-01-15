package com.shop.demo.service;

import com.shop.demo.constant.ItemSellStatus;
import com.shop.demo.dto.OrderDto;
import com.shop.demo.entity.Item;
import com.shop.demo.entity.Member;
import com.shop.demo.entity.Order;
import com.shop.demo.entity.OrderItem;
import com.shop.demo.repository.ItemRepository;
import com.shop.demo.repository.MemberRepository;
import com.shop.demo.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("주문 테스트")
  void order(){
    Item item = saveItem();
    Member member = saveMember();

    OrderDto orderDto = new OrderDto();
    orderDto.setCount(10);
    orderDto.setItemId(item.getId());

    Long orderId = orderService.order(orderDto, member.getEmail());
    Order order = orderRepository.findById(orderId)
        .orElseThrow(EntityNotFoundException::new);

    List<OrderItem> orderItems = order.getOrderItems();

    int totalPrice = orderDto.getCount()*item.getPrice();

    assertEquals(totalPrice, order.getTotalPrice());
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