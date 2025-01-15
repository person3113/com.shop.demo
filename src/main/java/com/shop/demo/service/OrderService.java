package com.shop.demo.service;

import com.shop.demo.dto.OrderDto;
import com.shop.demo.entity.*;
import com.shop.demo.repository.ItemRepository;
import com.shop.demo.repository.MemberRepository;
import com.shop.demo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final OrderRepository orderRepository;

  public Long order(OrderDto orderDto, String email){

    Item item = itemRepository.findById(orderDto.getItemId())
        .orElseThrow(EntityNotFoundException::new);

    Member member = memberRepository.findByEmail(email);

    List<OrderItem> orderItemList = new ArrayList<>();
    OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
    orderItemList.add(orderItem);

    Order order = Order.createOrder(member, orderItemList);
    orderRepository.save(order);

    return order.getId();
  }

}
