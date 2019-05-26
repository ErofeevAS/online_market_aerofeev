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
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public ItemRestDTO toRestDTO(Item item) {
        Long id = item.getId();
        String description = item.getDescription();
        String name = item.getName();
        String uniqueNumber = item.getUniqueNumber();
        BigDecimal price = item.getPrice();
        ItemRestDTO itemRestDTO = new ItemRestDTO();
        itemRestDTO.setId(id);
        itemRestDTO.setDescription(description);
        itemRestDTO.setName(name);
        itemRestDTO.setUniqueNumber(uniqueNumber);
        itemRestDTO.setPrice(price);
        return itemRestDTO;
    }

    @Override
    public Item fromRestDTO(ItemRestDTO itemRestDTO) {
        Long id = itemRestDTO.getId();
        String description = itemRestDTO.getDescription();
        String name = itemRestDTO.getName();
        String uniqueNumber = itemRestDTO.getUniqueNumber();
        BigDecimal price = itemRestDTO.getPrice();
        Item item = new Item();
        item.setId(id);
        item.setDescription(description);
        item.setName(name);
        item.setUniqueNumber(uniqueNumber);
        item.setPrice(price);
        item.setDeleted(false);
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
        ItemDTO itemDTO = toDTO(item);
        String uniqueNumber = UUID.randomUUID().toString();
        itemDTO.setUniqueNumber(uniqueNumber);
        String originalItemName = itemDTO.getName();
        String nameForCopy = getNameForCopy(originalItemName);
        itemDTO.setName(nameForCopy);
        Item copyOfItem = fromDTO(itemDTO);
        copyOfItem.setDeleted(false);
        copyOfItem.setId(null);
        User user = item.getUser();
        copyOfItem.setUser(user);
        return copyOfItem;
    }

    private String getNameForCopy(String originalName) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String nameWithoutSuffixOfCopy = originalName.split(SUFFIX_FOR_COPY)[0];
        return nameWithoutSuffixOfCopy + SUFFIX_FOR_COPY + timestamp.toString();
    }
}
