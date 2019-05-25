package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.UserConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemConverterImpl implements ItemConverter {
    private static final int SHORT_DESCRIPTION_LENGTH = 200;
    private final UserConverter userConverter;

    public ItemConverterImpl(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ItemDTO toDTO(Item item) {
        Long id = item.getId();
        String name = item.getName();
        BigDecimal price = item.getPrice();
        String description = item.getDescription();
        String uniqueNumber = item.getUniqueNumber();
        User user = item.getUser();
        UserDTO userDTO = userConverter.toDTO(user);
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setUser(userDTO);
        itemDTO.setId(id);
        itemDTO.setName(name);
        itemDTO.setPrice(price);
        itemDTO.setDescription(description);
        itemDTO.setUniqueNumber(uniqueNumber);
        String shortDescription = description;
        if (shortDescription.length() > SHORT_DESCRIPTION_LENGTH) {
            shortDescription = description.substring(0, SHORT_DESCRIPTION_LENGTH);
        }
        itemDTO.setShortDescription(shortDescription);
        return itemDTO;
    }

    @Override
    public Item fromDTO(ItemDTO itemDTO) {
        Long id = itemDTO.getId();
        String name = itemDTO.getName();
        BigDecimal price = itemDTO.getPrice();
        String description = itemDTO.getDescription();
        String uniqueNumber = itemDTO.getUniqueNumber();
        UserDTO userDTO = itemDTO.getUser();
        User user = userConverter.fromDTO(userDTO);
        Item item = new Item();
        item.setUser(user);
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);
        item.setUniqueNumber(uniqueNumber);
        return item;
    }

    @Override
    public List<ItemDTO> toListDTO(List<Item> items) {
        return items.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
