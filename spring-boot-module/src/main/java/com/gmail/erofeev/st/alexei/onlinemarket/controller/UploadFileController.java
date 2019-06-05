package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemXMLService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.xml.ItemXMLDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.XSD_FILE_PATH;

@Controller
public class UploadFileController {
    private final ItemXMLService itemXMLService;
    private final ItemService itemService;

    public UploadFileController(ItemXMLService itemXMLService, ItemService itemService) {
        this.itemXMLService = itemXMLService;
        this.itemService = itemService;
    }

    @GetMapping("/upload")
    public String uploadFile() {
        return "uploadFile";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        modelMap.addAttribute("file", file);
        itemXMLService.isValidByXsdScheme(file, XSD_FILE_PATH);
        List<ItemXMLDTO> itemsXML = itemXMLService.importFromXMLFile(file);
        itemService.importItem(itemsXML);
        return "uploadFile";
    }
}

