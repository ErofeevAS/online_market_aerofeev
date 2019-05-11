package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldSucceedWith200ForLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSucceedWith200ForAboutPage() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"Administrator"})
    public void shouldGetUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"Administrator"})
    public void shouldGetUsersPageWithSomeUsers() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedWith200ForUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "Customer")
    public void shouldForbidAccess() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));

    }
}