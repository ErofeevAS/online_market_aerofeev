package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.RequestParamsValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.ItemService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@Validated
public class RestItemController {
    private final ItemService itemService;
    private final RequestParamsValidator requestParamsValidator;

    public RestItemController(ItemService itemService, RequestParamsValidator requestParamsValidator) {
        this.itemService = itemService;
        this.requestParamsValidator = requestParamsValidator;
    }

    @GetMapping
    public List<ItemDTO> getItems(@RequestParam(defaultValue = "1", required = false) String offset,
                                  @RequestParam(defaultValue = "10", required = false) String amount) {
        int intOffset = requestParamsValidator.validateIntRest(offset);
        int intAmount = requestParamsValidator.validateIntRest(amount);
        return itemService.getItemsForRest(intOffset, intAmount);
    }

    @GetMapping("/{id}")
    public ItemDTO getItem(@PathVariable String id) {
        Long validatedId = requestParamsValidator.validateLongRest(id);
        return itemService.findItemByIdForRest(validatedId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem(@PathVariable String id) {
        Long validatedId = requestParamsValidator.validateLongRest(id);
        itemService.deleteByIdForRest(validatedId);
        return ResponseEntity.status(HttpStatus.OK).body("item was deleted");
    }

    @PostMapping
    public ItemDTO saveItem(@RequestBody @Valid ItemDTO itemRestDTO) {
        return itemService.saveItem(itemRestDTO);
    }
}
