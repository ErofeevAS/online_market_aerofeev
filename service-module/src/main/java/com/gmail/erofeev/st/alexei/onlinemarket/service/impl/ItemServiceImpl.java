package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {
    private static final String SUFFIX_FOR_COPY = " -copy ";
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;

    public ItemServiceImpl(ItemRepository itemRepository, ItemConverter itemConverter) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
    }

    @Override
    @Transactional
    public PageDTO<ItemDTO> getItems(int page, int amount) {
        Integer amountOfEntity = itemRepository.getAmountOfEntity();
        int maxPages = (Math.round(amountOfEntity / amount) + 1);
        int offset = getOffset(page, maxPages, amount);
        List<Item> items = itemRepository.findItems(offset, amount);
        List<ItemDTO> itemListDTO = itemConverter.toListDTO(items);
        PageDTO<ItemDTO> pageDTO = new PageDTO<>();
        pageDTO.setAmountOfPages(maxPages);
        pageDTO.setList(itemListDTO);
        return pageDTO;
    }

    @Override
    @Transactional
    public ItemDTO findById(Long id) {
        Item item = itemRepository.findById(id);
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Item item = itemRepository.findById(id);
        itemRepository.remove(item);
    }

    @Override
    @Transactional
    public void copyItem(Long id) {
        Item item = itemRepository.findById(id);
        ItemDTO itemDTO = itemConverter.toDTO(item);
        String uniqueNumber = UUID.randomUUID().toString();
        itemDTO.setUniqueNumber(uniqueNumber);
        String originalItemName = itemDTO.getName();
        String nameForCopy = getNameForCopy(originalItemName);
        itemDTO.setName(nameForCopy);
        Item copyOfItem = itemConverter.fromDTO(itemDTO);
        copyOfItem.setId(null);
        User user = item.getUser();
        copyOfItem.setUser(user);
        itemRepository.persist(copyOfItem);
    }

    private int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }

    private String getNameForCopy(String originalName) {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String nameWithoutSuffixOfCopy = originalName.split(SUFFIX_FOR_COPY)[0];
        return nameWithoutSuffixOfCopy + SUFFIX_FOR_COPY + timestamp.toString();
    }
}
