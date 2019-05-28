package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void accessDeniedTestForUnauthorizedUserOnUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void accessDeniedTestForUnauthorizedUserOnReviewsPage() throws Exception {
        mockMvc.perform(get("/reviews"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void wrongLoginTestForUser() throws Exception {
        mockMvc.perform(formLogin().user("wrong@gmail.com").password("wrong_password"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }
}
