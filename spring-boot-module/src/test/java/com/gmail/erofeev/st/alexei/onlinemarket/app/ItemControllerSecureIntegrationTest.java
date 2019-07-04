package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
    public void shouldHaveButtonForItemsForCustomer() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='itemsButton']").nodeCount(1));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldAccessDeniedForAdmin() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldHaveAccessToItemsPageForCustomer() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldReturnToItemPage() throws Exception {
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("item"));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToDeleteItemPageForSale() throws Exception {
        mockMvc.perform(post("/items/3/delete"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/items"));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToCopyItemPageForSale() throws Exception {
        mockMvc.perform(post("/items/2/copy"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/items"));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToDeleteItemPageForCustomer() throws Exception {
        mockMvc.perform(post("/items/1/delete"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToCopyItemPageForCustomer() throws Exception {
        mockMvc.perform(post("/items/2/copy"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }
}
