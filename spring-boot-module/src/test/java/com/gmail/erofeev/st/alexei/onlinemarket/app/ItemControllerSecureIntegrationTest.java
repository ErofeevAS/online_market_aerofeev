package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.erofeev.st.alexei.onlinemarket.controller.util.RoleConstant.REDIRECT_PAGE;
import static com.gmail.erofeev.st.alexei.onlinemarket.controller.util.RoleConstant.ROLE_ADMIN;
import static com.gmail.erofeev.st.alexei.onlinemarket.controller.util.RoleConstant.ROLE_CUSTOMER;
import static com.gmail.erofeev.st.alexei.onlinemarket.controller.util.RoleConstant.ROLE_SALE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerSecureIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveButtonForItems() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='itemsButton']").nodeCount(1));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveButtonToItemsForCustomer() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='itemsButton']").nodeCount(0));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldAccessDeniedForAdmin() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_PAGE));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldAccessDeniedForCustomer() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_PAGE));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldReturnToItemPage() throws Exception {
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("item"));
    }
}
