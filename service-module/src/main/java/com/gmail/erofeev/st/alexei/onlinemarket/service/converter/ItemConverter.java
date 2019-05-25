package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;

import java.util.List;

public interface ItemConverter {
    ItemDTO toDTO(Item item);

    Item fromDTO(ItemDTO itemDTO);

    List<ItemDTO> toListDTO(List<Item> items);

}
