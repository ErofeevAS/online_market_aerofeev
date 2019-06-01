package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

public class ItemDetailsDTO {
    private ItemDTO itemDTO;
    private String shortDescription;

    public ItemDTO getItemDTO() {
        return itemDTO;
    }

    public void setItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
