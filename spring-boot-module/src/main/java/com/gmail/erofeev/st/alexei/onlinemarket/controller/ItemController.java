package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.FrontEndValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDetailsDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    private final ItemService itemService;
    private final FrontEndValidator frontEndValidator;

    public ItemController(ItemService itemService, FrontEndValidator frontEndValidator) {
        this.itemService = itemService;
        this.frontEndValidator = frontEndValidator;
    }

    @GetMapping("/items")
    public String getItems(@RequestParam(defaultValue = "1", required = false) String page,
                           @RequestParam(defaultValue = "10", required = false) String size,
                           Model model) {
        Paginator paginator = new Paginator(page, size);
        PageDTO<ItemDetailsDTO> pageDTO = itemService.getItems(paginator.getPage(), paginator.getSize(), false);
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("items", pageDTO.getList());
        model.addAttribute("paginator", paginator);
        return "items";
    }

    @GetMapping("/items/{id}")
    public String viewItem(@PathVariable String id, Model model) {
        Long validateId = frontEndValidator.validateId(id);
        ItemDetailsDTO item = itemService.findById(validateId);
        model.addAttribute("item", item);
        return "item";
    }

    @PostMapping("/items/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteById(id);
        return "redirect:/items";
    }

    @PostMapping("/items/{id}/copy")
    public String copyItem(@PathVariable Long id) {
        itemService.copyItem(id);
        return "redirect:/items";
    }
}
