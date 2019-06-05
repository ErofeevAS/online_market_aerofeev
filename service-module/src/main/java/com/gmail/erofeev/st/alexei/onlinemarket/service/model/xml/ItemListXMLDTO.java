package com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemListXMLDTO {
    @XmlElement(name = "item")
    private List<ItemXMLDTO> items;

    public List<ItemXMLDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemXMLDTO> items) {
        this.items = items;
    }

}