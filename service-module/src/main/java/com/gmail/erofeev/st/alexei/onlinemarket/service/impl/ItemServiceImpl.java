package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.ItemRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.ItemConverter;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml.ItemXMLDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceImpl extends GenericService<ItemDetailsDTO> implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;

    public ItemServiceImpl(ItemRepository itemRepository, ItemConverter itemConverter) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
    }

    @Override
    @Transactional
    public PageDTO<ItemDetailsDTO> getItems(int page, int amount, boolean showDeleted) {
        Integer amountOfEntity = itemRepository.getAmountOfEntity(showDeleted);
        int maxPages = getMaxPages(amountOfEntity, amount);
        int offset = getOffset(page, maxPages, amount);
        List<Item> items = itemRepository.findItems(offset, amount, showDeleted);
        List<ItemDetailsDTO> itemListDTO = itemConverter.toListDetailsDTO(items);
        return getPageDTO(itemListDTO, maxPages);
    }

    @Override
    @Transactional
    public ItemDetailsDTO findById(Long id) {
        Item item = itemRepository.findById(id);
        isExist(item, id);
        return itemConverter.toDetailsDTO(item);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Item item = itemRepository.findById(id);
        isExist(item, id);
        itemRepository.remove(item);
    }

    @Override
    @Transactional
    public void deleteByIdForRest(Long id) {
        Item item = itemRepository.findById(id);
        isExistForRest(item, id);
        itemRepository.remove(item);
    }

    @Override
    @Transactional
    public void copyItem(Long id) {
        Item item = itemRepository.findById(id);
        isExist(item, id);
        Item copyOfItem = itemConverter.copyItem(item);
        itemRepository.persist(copyOfItem);
    }

    @Override
    @Transactional
    public List<ItemDTO> getItemsForRest(int offset, int amount) {
        List<Item> items = itemRepository.findItems(offset, amount, false);
        return itemConverter.toListDTO(items);
    }

    @Override
    @Transactional
    public ItemDTO findItemByIdForRest(Long id) {
        Item item = itemRepository.findById(id);
        isExistForRest(item, id);
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO saveItem(ItemDTO itemRestDTO) {
        Item item = itemConverter.fromDTO(itemRestDTO);
        if (item.getUniqueNumber() == null || item.getUniqueNumber().equals("")) {
            item.setUniqueNumber(UUID.randomUUID().toString());
        }
        itemRepository.persist(item);
        logger.debug(String.format("Item with name:%s was saved", itemRestDTO.getName()));
        return itemConverter.toDTO(item);
    }

    @Override
    @Transactional
    public void importItem(List<ItemXMLDTO> itemsXML) {
        List<Item> items = itemConverter.fromListItemXML(itemsXML);
        for (Item item : items) {
            Item itemFromDataBase = itemRepository.findByUUID(item.getUniqueNumber());
            if (itemFromDataBase == null) {
                itemRepository.persist(item);
            } else {
                itemConverter.updateDataBaseEntity(item, itemFromDataBase);
                itemRepository.merge(itemFromDataBase);
            }
        }
    }

    private void isExist(Item item, Long id) {
        String message = String.format("Item with id:%s not found. ", id);
        if (item == null) {
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
        if (item.getDeleted()) {
            logger.error(message);
            throw new EntityNotFoundException(message);
        }
    }

    private void isExistForRest(Item item, Long id) {
        String message = String.format("Item with id:%s not found. ", id);
        if (item == null) {
            logger.error(message);
            throw new RestEntityNotFoundException(message);
        }
        if (item.getDeleted()) {
            logger.error(message);
            throw new RestEntityNotFoundException(message);
        }
    }
}
