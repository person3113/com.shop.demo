package com.shop.demo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.demo.constant.ItemSellStatus;
import com.shop.demo.dto.ItemSearchDto;
import com.shop.demo.dto.MainItemDto;
import com.shop.demo.dto.QMainItemDto;
import com.shop.demo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.shop.demo.entity.QItem.*;
import static com.shop.demo.entity.QItemImg.itemImg;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

  private JPAQueryFactory queryFactory;

  public ItemRepositoryCustomImpl(EntityManager em){
    this.queryFactory = new JPAQueryFactory(em);
  }

  private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
    return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
  }

  private BooleanExpression regDtsAfter(String searchDateType){

    LocalDateTime dateTime = LocalDateTime.now();

    if(StringUtils.equals("all", searchDateType) || searchDateType == null){
      return null;
    } else if(StringUtils.equals("1d", searchDateType)){
      dateTime = dateTime.minusDays(1);
    } else if(StringUtils.equals("1w", searchDateType)){
      dateTime = dateTime.minusWeeks(1);
    } else if(StringUtils.equals("1m", searchDateType)){
      dateTime = dateTime.minusMonths(1);
    } else if(StringUtils.equals("6m", searchDateType)){
      dateTime = dateTime.minusMonths(6);
    }

    return item.regTime.after(dateTime);
  }

  private BooleanExpression searchByLike(String searchBy, String searchQuery){

    if(StringUtils.equals("itemNm", searchBy)){
      return item.itemNm.like("%" + searchQuery + "%");
    } else if(StringUtils.equals("createdBy", searchBy)){
      return item.createdBy.like("%" + searchQuery + "%");
    }

    return null;
  }

  @Override
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

    List<Item> content = queryFactory
        .selectFrom(item)
        .where(regDtsAfter(itemSearchDto.getSearchDateType()),
            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
            searchByLike(itemSearchDto.getSearchBy(),
                itemSearchDto.getSearchQuery()))
        .orderBy(item.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = queryFactory.select(Wildcard.count).from(item)
        .where(regDtsAfter(itemSearchDto.getSearchDateType()),
            searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
            searchByLike(itemSearchDto.getSearchBy(),
                itemSearchDto.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression itemNmLike(String searchQuery){
    return StringUtils.isEmpty(searchQuery) ? null : item.itemNm.like("%" + searchQuery + "%");
  }

  @Override
  public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//    QItem item = QItem.item;
//    QItemImg itemImg = QItemImg.itemImg;

    List<MainItemDto> content = queryFactory
        .select(
            new QMainItemDto(
                item.id,
                item.itemNm,
                item.itemDetail,
                itemImg.imgUrl,
                item.price)
        )
        .from(itemImg)
        .join(itemImg.item, item)
        .where(itemImg.repimgYn.eq("Y"))
        .where(itemNmLike(itemSearchDto.getSearchQuery()))
        .orderBy(item.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = queryFactory
        .select(Wildcard.count)
        .from(itemImg)
        .join(itemImg.item, item)
        .where(itemImg.repimgYn.eq("Y"))
        .where(itemNmLike(itemSearchDto.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(content, pageable, total);
  }

}
