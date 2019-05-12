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

    public Paginator(int current, int maxPage, int size) {
        this.setMaxPage(maxPage);
        this.setPage(current);
        this.setSize(size);
    }

    public void validate(int current, int maxPage, int size) {
        this.setMaxPage(maxPage);
        this.setPage(current);
        this.setSize(size);
    }

    public Paginator() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page > maxPage) {
            page = maxPage;
        }
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
