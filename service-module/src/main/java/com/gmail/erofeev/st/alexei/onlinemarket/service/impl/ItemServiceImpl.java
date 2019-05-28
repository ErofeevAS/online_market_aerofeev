package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemRestDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, ItemConverter itemConverter, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
        this.userRepository = userRepository;
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
        if (item == null) {
            String message = String.format("Item with id:%s not found", id);
            logger.error(message);
            throw new ServiceException(message);
        }
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            logger.debug(String.format("Item with id:%s not found. Can not delete item", id));
            throw new EntityNotFoundException(String.format("Item with id:%s not found. Can not delete item", id));
        }
        itemRepository.remove(item);
    }

    @Override
    @Transactional
    public void copyItem(Long id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            logger.debug(String.format("Item with id:%s not found. Can not copy item", id));
            throw new EntityNotFoundException(String.format("Item with id:%s not found. Can not copy item", id));
        }
        Item copyOfItem = itemConverter.copyItem(item);
        itemRepository.persist(copyOfItem);
    }

    @Override
    @Transactional
    public List<ItemRestDTO> getItemsForRest(int offset, int amount) {
        List<Item> items = itemRepository.getEntities(offset, amount);
        return itemConverter.toListRestDTO(items);
    }

    @Override
    @Transactional
    public ItemRestDTO findRestItemById(Long validatedId) {
        Item item = itemRepository.findById(validatedId);
        if (item == null) {
            logger.debug(String.format("Item with id:%s not found", validatedId));
            throw new EntityNotFoundException(String.format("Item with id:%s not found", validatedId));
        }
        return itemConverter.toRestDTO(item);
    }

    @Override
    @Transactional
    public ItemRestDTO saveItem(Long userId, ItemRestDTO itemRestDTO) {
        User user = userRepository.findById(userId);
        Item item = itemConverter.fromRestDTO(itemRestDTO);
        item.setUser(user);
        itemRepository.persist(item);
        logger.debug(String.format("Item with name:%s was saved for user with id:%s", itemRestDTO.getName(), userId));
        return itemConverter.toRestDTO(item);
    }

    private int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }
}
