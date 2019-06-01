package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

public abstract class AbstractService {
    int getOffset(int page, int maxPages, int amount) {
        if (page > maxPages) {
            page = maxPages;
        }
        return (page - 1) * amount;
    }

    int getMaxPages(int amountOfEntity, int amount) {
        return (Math.round(amountOfEntity / amount) + 1);
    }
}
