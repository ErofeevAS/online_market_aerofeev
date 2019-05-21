package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;

@Component
public class Paginator {
    private int page;
    private int maxPage;
    private int size;
    private final List<String> droppedListValues = asList("2", "5", "10", "20");

    private String url;

    public Paginator(String stringPage, String stringSize) {
        validatePage(stringPage);
        validateSize(stringSize);
    }

    public int validatePage(String stringPage) {
        try {
            page = Integer.parseInt(stringPage);
        } catch (NumberFormatException e) {
            page = 1;
        }
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    public int validateSize(String stringSize) {
        try {
            size = Integer.parseInt(stringSize);
        } catch (NumberFormatException e) {
            size = 10;
        }
        if (size > 100 || size < 1) {
            size = 10;
        }
        return size;
    }

    public Paginator() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getDroppedListValues() {
        return droppedListValues;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
