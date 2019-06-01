package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.UserRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ItemServiceImpl extends AbstractService implements ItemService {
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
    public PageDTO<ItemDetailsDTO> getItems(int page, int amount) {
        Integer amountOfEntity = itemRepository.getAmountOfEntity();
        int maxPages = getMaxPages(amountOfEntity, amount);
        int offset = getOffset(page, maxPages, amount);
        List<Item> items = itemRepository.findItems(offset, amount);
        List<ItemDetailsDTO> itemListDTO = itemConverter.toListDetailsDTO(items);
        PageDTO<ItemDetailsDTO> pageDTO = new PageDTO<>();
        pageDTO.setAmountOfPages(maxPages);
        pageDTO.setList(itemListDTO);
        return pageDTO;
    }

    @Override
    @Transactional
    public ItemDetailsDTO findById(Long id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            String message = String.format("Item with id:%s not found", id);
            logger.error(message);
            throw new ServiceException(message);
        }
        return itemConverter.toDetailsDTO(item);
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
    public List<ItemDTO> getItemsForRest(int offset, int amount) {
        List<Item> items = itemRepository.getEntities(offset, amount);
        return itemConverter.toListDTO(items);
    }

    @Override
    @Transactional
    public ItemDTO findRestItemById(Long validatedId) {
        Item item = itemRepository.findById(validatedId);
        if (item == null) {
            logger.debug(String.format("Item with id:%s not found", validatedId));
            throw new EntityNotFoundException(String.format("Item with id:%s not found", validatedId));
        }
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO saveItem(Long userId, ItemDTO itemRestDTO) {
        User user = userRepository.findById(userId);
        Item item = itemConverter.fromDTO(itemRestDTO);
        item.setUser(user);
        itemRepository.persist(item);
        logger.debug(String.format("Item with name:%s was saved for user with id:%s", itemRestDTO.getName(), userId));
        return itemConverter.toDTO(item);
    }
}
