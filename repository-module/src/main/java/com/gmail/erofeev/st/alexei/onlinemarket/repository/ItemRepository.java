package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Item;

import java.util.List;

public interface ItemRepository extends GenericRepository<Long, Item> {
    List<Item> findItems(Integer offset, Integer amount);
}
