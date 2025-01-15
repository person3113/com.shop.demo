package com.shop.demo.repository;

import com.shop.demo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,ItemRepositoryCustom {

  List<Item> findByItemNm(String itemNm);

  List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

  List<Item> findByPriceLessThan(Integer price);

  List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

  @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
  List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
}
