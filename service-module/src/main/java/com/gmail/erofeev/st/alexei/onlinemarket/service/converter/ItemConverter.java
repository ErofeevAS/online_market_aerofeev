package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml.ItemXMLDTO;

import java.util.List;

public interface ItemConverter {
    ItemDetailsDTO toDetailsDTO(Item item);

    Item fromDetailsDTO(ItemDetailsDTO itemDetailsDTO);

    List<ItemDetailsDTO> toListDetailsDTO(List<Item> items);

    ItemDTO toDTO(Item item);

    Item fromDTO(ItemDTO itemRestDTO);

    List<ItemDTO> toListDTO(List<Item> items);

    Item copyItem(Item item);

    List<Item> fromListItemXML(List<ItemXMLDTO> itemsXML);

    Item fromXMLDTO(ItemXMLDTO itemXMLDTO);

    void updateDataBaseEntity(Item item, Item itemFromDataBase);
}
