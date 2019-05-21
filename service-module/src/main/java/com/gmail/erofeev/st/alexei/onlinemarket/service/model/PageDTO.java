package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.util.ArrayList;
import java.util.List;

public class PageDTO<T> {
    private int amountOfPages;
    private List<T> list = new ArrayList<>();

    public int getAmountOfPages() {
        return amountOfPages;
    }

    public void setAmountOfPages(int amountOfPages) {
        this.amountOfPages = amountOfPages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}