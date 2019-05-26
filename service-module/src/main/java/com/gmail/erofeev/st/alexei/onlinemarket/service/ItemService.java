package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;

import java.util.List;

public interface ItemService {
    PageDTO<ItemDTO> getItems(int page, int amount);

    ItemDTO findById(Long id);

    void deleteById(Long id);

    void copyItem(Long id);

    List<ItemRestDTO> getItemsForRest(int offset, int amount);

    ItemRestDTO findRestItemById(Long validatedId);

    ItemRestDTO saveItem(Long userId, ItemRestDTO itemRestDTO);
}
