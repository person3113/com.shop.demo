package com.shop.demo.service;

import com.shop.demo.dto.ItemFormDto;
import com.shop.demo.entity.Item;
import com.shop.demo.entity.ItemImg;
import com.shop.demo.repository.ItemImgRepository;
import com.shop.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final ItemImgService itemImgService;
  private final ItemImgRepository itemImgRepository;

  public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

    //상품 등록
    Item item = itemFormDto.createItem();
    itemRepository.save(item);

    //이미지 등록
    for(int i=0;i<itemImgFileList.size();i++){
      ItemImg itemImg = new ItemImg();
      itemImg.setItem(item);

      if(i == 0)
        itemImg.setRepimgYn("Y");
      else
        itemImg.setRepimgYn("N");

      itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
    }

    return item.getId();
  }

}
