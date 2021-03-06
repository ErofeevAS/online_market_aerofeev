package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderDTO {
    private String uniqueNumber;
    private OrderStatusEnum status;
    private String itemName;
    private int amountOfItem;
    private BigDecimal totalPrice;
    private Timestamp createdDate;

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmountOfItem() {
        return amountOfItem;
    }

    public void setAmountOfItem(int amountOfItem) {
        this.amountOfItem = amountOfItem;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
