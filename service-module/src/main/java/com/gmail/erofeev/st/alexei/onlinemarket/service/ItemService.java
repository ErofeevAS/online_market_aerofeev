package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;

public interface ItemService {
    PageDTO<ItemDTO> getItems(int page, int amount);

    ItemDTO findById(Long id);

    void deleteById(Long id);

    void copyItem(Long id);
}
