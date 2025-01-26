package com.shop.demo.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.demo.constant.ItemSellStatus;
import com.shop.demo.entity.Item;
import com.shop.demo.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.shop.demo.entity.QItem.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("상품 저장 테스트")
  void createItemTest() {
    // given
    Item item = new Item();
    item.setItemNm("테스트 상품");
    item.setPrice(10000);
    item.setItemDetail("테스트 상품 상세 설명");
    item.setItemSellStatus(ItemSellStatus.SELL);
    item.setStockNumber(100);
    item.setRegTime(LocalDateTime.now());
    item.setUpdateTime(LocalDateTime.now());

    // when
    Item savedItem = itemRepository.save(item);

    // then
    assertThat(savedItem.getItemNm()).isEqualTo(item.getItemNm());
  }

  @Test
  @DisplayName("상품명 조회 테스트")
  void findByItemNmTest(){
    //given
    createItemList();

    //when
    List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");

    //then
    for(int i=0;i<itemList.size();i++){
      assertThat(itemList.get(i).getItemNm()).isEqualTo("테스트 상품1");
    }
  }

  @Test
  @DisplayName("상품명, 상품상세설명 or 테스트")
  void findByItemNmOrItemDetailTest(){
    //given
    createItemList();

    //when
    List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");

    //then
    assertThat(itemList.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("가격 LessThan 테스트")
  void findByPriceLessThanTest(){
    //given
    createItemList();

    //when
    List<Item> itemList = itemRepository.findByPriceLessThan(10005);

    //then
    assertThat(itemList.size()).isEqualTo(4);
  }

  @Test
  @DisplayName("가격 내림차순 조회 테스트")
  void findByPriceLessThanOrderByPriceDesc(){
    //given
    createItemList();

    //when
    List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);

    //then
    int expectedPrice=10004;
    for(Item item : itemList){
      assertThat(item.getPrice()).isEqualTo(expectedPrice);
      --expectedPrice;
    }
  }

  @Test
  @DisplayName("@Query를 이용한 상품 조회 테스트")
  void findByItemDetailTest(){
    //given
    createItemList();

    //when
    List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");

    //then
    assertThat(itemList.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("Querydsl 조회 테스트1")
  void queryDslTest(){
    //given
    createItemList();
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    //when
    List<Item> itemList = queryFactory.selectFrom(item)
        .where(item.itemSellStatus.eq(ItemSellStatus.SELL))
        .where(item.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
        .orderBy(item.price.desc())
        .fetch();

    for(Item item : itemList){
      System.out.println(item.toString());
    }
  }

  private void createItemList(){
    for(int i=1;i<=10;i++){
      Item item = new Item();

      item.setItemNm("테스트 상품" + i);
      item.setPrice(10000 + i);
      item.setItemDetail("테스트 상품 상세 설명" + i);
      item.setItemSellStatus(ItemSellStatus.SELL);
      item.setStockNumber(100);
      item.setRegTime(LocalDateTime.now());
      item.setUpdateTime(LocalDateTime.now());

      itemRepository.save(item);
    }
  }

}