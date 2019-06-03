package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemXMLService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml.ItemXMLDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class UploadFileController {
    private final ItemXMLService itemXMLService;
    private final ItemService itemService;


    public UploadFileController(ItemXMLService itemXMLService,
                                ItemService itemService) {
        this.itemXMLService = itemXMLService;
        this.itemService = itemService;
    }

    @GetMapping("/upload")
    public String uploadFile() {
        return "uploadFile";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, ModelMap modelMap) throws IOException {
        modelMap.addAttribute("file", file);
        InputStream fileContent = file.getInputStream();
        File xsdScheme = new ClassPathResource("items.xsd").getFile();
        itemXMLService.isValidByXsdScheme(fileContent, xsdScheme);
        InputStream fileInputStream = file.getInputStream();
        List<ItemXMLDTO> itemsXML = itemXMLService.importFromXMLFile(fileInputStream, xsdScheme);
        itemService.importItem(itemsXML);
        return "uploadFile";
    }
}

