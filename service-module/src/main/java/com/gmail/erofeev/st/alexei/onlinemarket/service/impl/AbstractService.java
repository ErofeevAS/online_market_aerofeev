package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;

import java.util.List;

public abstract class AbstractService<T> {
    int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }

    int getMaxPages(int amountOfEntity, int amount) {
        return (Math.round(amountOfEntity / amount) + 1);
    }

    PageDTO<T> getPageDTO(List<T> listDTo, Integer maxPages) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setList(listDTo);
        pageDTO.setAmountOfPages(maxPages);
        return pageDTO;
    }
}
