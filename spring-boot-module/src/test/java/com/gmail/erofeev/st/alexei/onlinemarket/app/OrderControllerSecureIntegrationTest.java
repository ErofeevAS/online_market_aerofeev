package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ADMIN_EMAIL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.CUSTOMER_EMAIL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.REDIRECT_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_ADMIN;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_CUSTOMER;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_SALE;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_SECURE_REST_API;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.SALE_EMAIL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerSecureIntegrationTest {
    private static final String TEST_ORDER_UUID = "1235b3e7-e79a-485c-a79e-2733ada95051";
    private static final Long TEST_CUSTOMER_USER_ID = 3L;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToOrderForSale() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToOrdersForCustomer() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldHaveAccessToOrderForCustomer() throws Exception {
        mockMvc.perform(get("/users/" + TEST_CUSTOMER_USER_ID + "/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldHaveAccessToOwnOrdersForCustomer() throws Exception {
        mockMvc.perform(get("/userorders"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/" + TEST_CUSTOMER_USER_ID + "/orders"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    public void shouldNotHaveAccessToOwnOrdersForAdmin() throws Exception {
        mockMvc.perform(get("/userorders"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldHaveAccessToOrderForAdmin() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void shouldHaveAccessToOrderForSecureApi() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToOrderDetailsForSale() throws Exception {
        mockMvc.perform(post("/orders/" + TEST_ORDER_UUID))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldHaveAccessToOrderDetailsForCustomer() throws Exception {
        mockMvc.perform(post("/orders/" + TEST_ORDER_UUID))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToOrderDetailsForAdmin() throws Exception {
        mockMvc.perform(post("/orders/" + TEST_ORDER_UUID))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldCreateNewOrderForCustomer() throws Exception {
        mockMvc.perform(post("/orders/sale/new")
                .param("itemId", "1")
                .param("amount", "2"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users/" + TEST_CUSTOMER_USER_ID + "/orders"));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldRedirectToWrongAmountPageIfAmountNotPositiveInteger() throws Exception {
        mockMvc.perform(post("/orders/sale/new")
                .param("itemId", "1")
                .param("amount", "-2"))
                .andExpect(status().isOk())
                .andExpect(view().name("wrongAmount"));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldNotHaveAccessToCreateNewOrderForSale() throws Exception {
        mockMvc.perform(post("/orders/sale/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToCreateNewOrderForAdmin() throws Exception {
        mockMvc.perform(post("/orders/sale/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldUpdateOrderStatusForSale() throws Exception {
        mockMvc.perform(post("/orders/" + TEST_ORDER_UUID + "/update")
                .param("orderStatus", "IN_PROGRESS"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/orders"));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldNotHaveAccessToUpdateOrderStatusForCustomer() throws Exception {
        mockMvc.perform(post("/orders/" + TEST_ORDER_UUID + "/update"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    public void shouldNotHaveAccessToUpdateOrderStatusForAdmin() throws Exception {
        mockMvc.perform(post("/orders/" + TEST_ORDER_UUID + "/update"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }
}
