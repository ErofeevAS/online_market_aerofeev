package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ItemDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RestItemControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldReturnTwoItems() throws Exception {
        String offset = "1";
        String amount = "2";
        String url = "/api/v1/items";
        MvcResult mvcResult = mvc.perform(get(url).param("offset", offset).param("amount", amount))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<ItemDTO> items = objectMapper.readValue(content, new TypeReference<List<ItemDTO>>() {
        });
        Assert.assertEquals(2, items.size());
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldReturnMessageWrongRequestParameter() throws Exception {
        String offset = "a";
        String amount = "2";
        String url = "/api/v1/items";
        mvc.perform(get(url).param("offset", offset).param("amount", amount))
                .andExpect(status().isConflict()).andReturn();
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldReturnItem() throws Exception {
        String url = "/api/v1/items/5";
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ItemDTO item = objectMapper.readValue(content, new TypeReference<ItemDTO>() {
        });
        boolean isExist = false;
        if (item.getId() == 5L) {
            isExist = true;
        }
        Assert.assertTrue(isExist);
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldNotFountItem() throws Exception {
        Long maxId = Long.MAX_VALUE;
        String url = "/api/v1/items/" + maxId;
        mvc.perform(get(url)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void deleteItemById() throws Exception {
        String offset = "0";
        String amount = String.valueOf(Integer.MAX_VALUE);
        String url = "/api/v1/items";
        MvcResult mvcResult = mvc.perform(get(url).param("offset", offset).param("amount", amount))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<ItemDTO> items = objectMapper.readValue(content, new TypeReference<List<ItemDTO>>() {
        });
        String urlForDelete = "/api/v1/items/1";
        mvc.perform(delete(urlForDelete))
                .andExpect(MockMvcResultMatchers.content().string("item was deleted"))
                .andExpect(status().isOk()).andReturn();

        mvcResult = mvc.perform(get(url).param("offset", offset).param("amount", amount))
                .andExpect(status().isOk()).andReturn();
        content = mvcResult.getResponse().getContentAsString();
        List<ItemDTO> itemsAfterDeleteOne = objectMapper.readValue(content, new TypeReference<List<ItemDTO>>() {
        });
        int amountOfItem = items.size();
        int amountOfItemsAfterDeleteOne = itemsAfterDeleteOne.size();
        Assert.assertEquals(1, amountOfItem - amountOfItemsAfterDeleteOne);
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldNotFountItemForDelete() throws Exception {
        Long maxId = Long.MAX_VALUE;
        String url = "/api/v1/items/" + maxId;
        mvc.perform(delete(url)).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @WithUserDetails(value = REST_API_EMAIL, userDetailsServiceBeanName = USER_DETAILS_SERVICE_REST_API)
    public void shouldSaveNewItemForUser() throws Exception {
        ItemDTO itemRestDTO = new ItemDTO();
        String name = "rest_test_item";
        String description = "rest_test_item_description";
        BigDecimal price = new BigDecimal("123.11");
        itemRestDTO.setPrice(price);
        itemRestDTO.setName(name);
        itemRestDTO.setDescription(description);
        itemRestDTO.setUniqueNumber(UUID.randomUUID().toString());
        String json = objectMapper.writeValueAsString(itemRestDTO);
        String url = "/api/v1/items";
        MvcResult mvcResult = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ItemDTO item = objectMapper.readValue(content, new TypeReference<ItemDTO>() {
        });
        Assert.assertNotNull(item.getId());
    }
}