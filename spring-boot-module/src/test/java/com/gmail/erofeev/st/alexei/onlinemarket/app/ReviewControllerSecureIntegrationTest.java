package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerSecureIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedWith200ForReviewsPage() throws Exception {
        mockMvc.perform(get("/reviews").param("page", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("reviews"));
    }

    @Test
    @WithMockUser(roles = "Sale")
    public void shouldAccessDenyForNotAdminForReviewsPage() throws Exception {
        mockMvc.perform(get("/reviews").param("page", "1").param("size", "10"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldGetFourTestReviewsFromReviewsPage() throws Exception {
        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='review']").nodeCount(3));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldRedirectToReviewsPageAfterDelete() throws Exception {
        mockMvc.perform(post("/reviews/1/delete"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/reviews"));
    }

    @Test
    @WithMockUser(roles = "Sale")
    public void shouldAccessDenyForNotAdminForDelete() throws Exception {
        mockMvc.perform(post("/reviews/1/delete"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }
}
