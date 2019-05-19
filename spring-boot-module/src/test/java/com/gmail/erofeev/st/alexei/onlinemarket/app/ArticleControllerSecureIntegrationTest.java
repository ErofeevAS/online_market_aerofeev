package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.gmail.erofeev.st.alexei.onlinemarket.service.CommentService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerSecureIntegrationTest {
    private static final String ROLE_ADMIN = "Administrator";
    private static final String ROLE_CUSTOMER = "Customer";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldSucceedWith200ForArticlesPage() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"))
                .andExpect(content().string(containsString("user@gmail.com")));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldSucceedRedirectOnArticlePage() throws Exception {
        mockMvc.perform(post("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("article"));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldGetFourTestArticlesFromPage() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='article']").nodeCount(4));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldForbidAccess() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }
}
