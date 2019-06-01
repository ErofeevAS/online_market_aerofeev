package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;

import java.util.List;

public interface ItemService {
    PageDTO<ItemDetailsDTO> getItems(int page, int amount);

    ItemDetailsDTO findById(Long id);

    void deleteById(Long id);

    void copyItem(Long id);

    List<ItemDTO> getItemsForRest(int offset, int amount);

    ItemDTO findRestItemById(Long validatedId);

    ItemDTO saveItem(Long userId, ItemDTO itemRestDTO);
}
