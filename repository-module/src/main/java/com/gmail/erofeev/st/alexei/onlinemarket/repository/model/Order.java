package com.gmail.erofeev.st.alexei.onlinemarket.repository.model;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.enums.OrderStatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "`order`")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("item_id")
    private Item item;
    @Column(name = "unique_number")
    private String uniqueNumber;
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;
    @Column
    private int amount;
    @Column(name = "deleted")
    private boolean isDeleted;
    @Column(name = "created_date")
    private Timestamp createdDate;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return amount == order.amount &&
                isDeleted == order.isDeleted &&
                Objects.equals(id, order.id) &&
                Objects.equals(uniqueNumber, order.uniqueNumber) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueNumber, status, amount, isDeleted);
    }
}
