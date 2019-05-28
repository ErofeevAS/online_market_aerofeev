package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Component
public class ItemConverterImpl implements ItemConverter {
    private static final int SHORT_DESCRIPTION_LENGTH = 200;
    private static final String SUFFIX_FOR_COPY = " -copy ";
    private final UserConverter userConverter;

    public ItemConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        String description = item.getDescription();
        itemDTO.setDescription(description);
        itemDTO.setUniqueNumber(item.getUniqueNumber());
        User user = item.getUser();
        UserDTO userDTO = userConverter.toDTO(user);
        itemDTO.setUser(userDTO);
        String shortDescription = description;
        if (shortDescription.length() > SHORT_DESCRIPTION_LENGTH) {
            shortDescription = description.substring(0, SHORT_DESCRIPTION_LENGTH);
        }
        itemDTO.setShortDescription(shortDescription);
        return itemDTO;
    }

    @Override
    public Item fromDTO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(itemDTO.getDescription());
        item.setUniqueNumber(itemDTO.getUniqueNumber());
        UserDTO userDTO = itemDTO.getUser();
        User user = userConverter.fromDTO(userDTO);
        item.setUser(user);
        return item;
    }

    @Override
    public List<ItemDTO> toListDTO(List<Item> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRestDTO toRestDTO(Item item) {
        ItemRestDTO itemRestDTO = new ItemRestDTO();
        itemRestDTO.setId(item.getId());
        itemRestDTO.setDescription(item.getDescription());
        itemRestDTO.setName(item.getName());
        itemRestDTO.setUniqueNumber(item.getUniqueNumber());
        itemRestDTO.setPrice(item.getPrice());
        return itemRestDTO;
    }

    @Override
    public Item fromRestDTO(ItemRestDTO itemRestDTO) {
        Item item = new Item();
        item.setId(itemRestDTO.getId());
        item.setDescription(itemRestDTO.getDescription());
        item.setName(itemRestDTO.getName());
        item.setUniqueNumber(itemRestDTO.getUniqueNumber());
        item.setPrice(itemRestDTO.getPrice());
        return item;
    }

    @Override
    public List<ItemRestDTO> toListRestDTO(List<Item> items) {
        return items.stream()
                .map(this::toRestDTO)
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
        copyOfItem.setUser(item.getUser());
        return copyOfItem;
    }

    private String getNameForCopy(String originalName) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String nameWithoutSuffixOfCopy = originalName.split(SUFFIX_FOR_COPY)[0];
        return nameWithoutSuffixOfCopy + SUFFIX_FOR_COPY + timestamp.toString();
    }
}
