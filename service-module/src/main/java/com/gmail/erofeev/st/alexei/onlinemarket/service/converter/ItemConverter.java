package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemRestDTO;

import java.util.List;

public interface ItemConverter {
    ItemDTO toDTO(Item item);

    Item fromDTO(ItemDTO itemDTO);

    List<ItemDTO> toListDTO(List<Item> items);

    ItemRestDTO toRestDTO(Item item);

    Item fromRestDTO(ItemRestDTO itemRestDTO);

    List<ItemRestDTO> toListRestDTO(List<Item> items);

    Item copyItem(Item item);
}
