package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.OrderRestDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_SECURE_REST_API;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestOrderControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldReturnOrders() throws Exception {
        String url = "/api/v1/orders";
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<OrderRestDTO> orders = objectMapper.readValue(content, new TypeReference<List<OrderRestDTO>>() {
        });
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldReturnMessageWrongRequestParameter() throws Exception {
        String offset = "a";
        String amount = "2";
        String url = "/api/v1/orders";
        mvc.perform(get(url).param("offset", offset).param("amount", amount))
                .andExpect(status().isConflict()).andReturn();
    }


    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldReturnOrderById() throws Exception {
        String url = "/api/v1/orders/1";
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        OrderRestDTO order = objectMapper.readValue(content, new TypeReference<OrderRestDTO>() {
        });
        Assert.assertNotNull(order);
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldNotFoundOrder() throws Exception {
        Long maxId = Long.MAX_VALUE;
        String url = "/api/v1/orders/" + maxId;
        mvc.perform(get(url)).andExpect(status().isNotFound()).andReturn();
    }
}