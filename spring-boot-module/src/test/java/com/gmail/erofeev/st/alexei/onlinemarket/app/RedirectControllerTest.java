package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedirectControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldSucceedWith200ForLoginPage() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSucceedWith200ForAboutPage() throws Exception {
        mvc.perform(get("/about"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSucceedWithRedirectOnLoginPage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void shouldSucceedWithRedirectOnLoginPageFromIndexPage() throws Exception {
        mvc.perform(get("/index"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }
}