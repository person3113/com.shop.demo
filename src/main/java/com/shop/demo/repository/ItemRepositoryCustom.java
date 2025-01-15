package com.shop.demo.repository;

import com.shop.demo.dto.ItemSearchDto;
import com.shop.demo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

  Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
