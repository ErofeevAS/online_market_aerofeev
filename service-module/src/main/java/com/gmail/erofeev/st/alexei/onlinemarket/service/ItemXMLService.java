package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml.ItemXMLDTO;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface ItemXMLService {
    List<ItemXMLDTO> importFromXMLFile(InputStream file, File xsd);

    void isValidByXsdScheme(InputStream file, File xsd);
}
