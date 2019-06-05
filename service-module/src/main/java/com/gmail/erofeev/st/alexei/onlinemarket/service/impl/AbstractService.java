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
        if (amountOfEntity < 1) {
            return 1;
        }
        if (amountOfEntity % amount == 0) {
            return amountOfEntity / amount;
        } else {
            return amountOfEntity / amount + 1;
        }
    }

    PageDTO<T> getPageDTO(List<T> listDTo, Integer maxPages) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setList(listDTo);
        pageDTO.setAmountOfPages(maxPages);
        return pageDTO;
    }
}
