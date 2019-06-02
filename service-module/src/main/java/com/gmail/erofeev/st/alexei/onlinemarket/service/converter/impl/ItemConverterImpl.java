package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDetailsDTO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Component
public class ItemConverterImpl implements ItemConverter {
    private static final int SHORT_DESCRIPTION_LENGTH = 200;
    private static final String SUFFIX_FOR_COPY = " -copy ";

    @Override
    public ItemDetailsDTO toDetailsDTO(Item item) {
        ItemDetailsDTO itemDetailsDTO = new ItemDetailsDTO();
        ItemDTO itemDTO = toDTO(item);
        itemDetailsDTO.setItemDTO(itemDTO);
        String description = item.getDescription();
        String shortDescription = description;
        if (shortDescription.length() > SHORT_DESCRIPTION_LENGTH) {
            shortDescription = description.substring(0, SHORT_DESCRIPTION_LENGTH);
        }
        itemDetailsDTO.setShortDescription(shortDescription);
        return itemDetailsDTO;
    }

    @Override
    public Item fromDetailsDTO(ItemDetailsDTO itemDetailsDTO) {
        ItemDTO itemDTO = itemDetailsDTO.getItemDTO();
        return fromDTO(itemDTO);
    }

    @Override
    public List<ItemDetailsDTO> toListDetailsDTO(List<Item> items) {
        return items.stream()
                .map(this::toDetailsDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemRestDTO = new ItemDTO();
        itemRestDTO.setId(item.getId());
        itemRestDTO.setDescription(item.getDescription());
        itemRestDTO.setName(item.getName());
        itemRestDTO.setUniqueNumber(item.getUniqueNumber());
        itemRestDTO.setPrice(item.getPrice());
        return itemRestDTO;
    }

    @Override
    public Item fromDTO(ItemDTO itemRestDTO) {
        Item item = new Item();
        item.setId(itemRestDTO.getId());
        item.setDescription(itemRestDTO.getDescription());
        item.setName(itemRestDTO.getName());
        item.setUniqueNumber(itemRestDTO.getUniqueNumber());
        item.setPrice(itemRestDTO.getPrice());
        return item;
    }

    @Override
    public List<ItemDTO> toListDTO(List<Item> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Item copyItem(Item item) {
        Item copyOfItem = new Item();
        copyOfItem.setUniqueNumber(randomUUID().toString());
        String nameForCopy = getNameForCopy(item.getName());
        copyOfItem.setName(nameForCopy);
        copyOfItem.setDescription(item.getDescription());
        copyOfItem.setPrice(item.getPrice());
        return copyOfItem;
    }

    private String getNameForCopy(String originalName) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String nameWithoutSuffixOfCopy = originalName.split(SUFFIX_FOR_COPY)[0];
        return nameWithoutSuffixOfCopy + SUFFIX_FOR_COPY + timestamp.toString();
    }
}
