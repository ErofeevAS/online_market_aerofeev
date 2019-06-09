package com.gmail.erofeev.st.alexei.onlinemarket.service;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml.ItemXMLDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ItemXMLService {
    List<ItemXMLDTO> importFromXMLFile(MultipartFile file);

    void isValidByXsdScheme(MultipartFile file, String xsdPath);
}
