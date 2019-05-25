package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.NewArticleDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerSecureIntegrationTest {
    private static final String ROLE_ADMIN = "Administrator";
    private static final String ROLE_CUSTOMER = "Customer";
    private static final String ROLE_SALE = "Sale";
    private static final String REDIRECT_PAGE = "/403";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("user@gmail.com")
    public void shouldSucceedWith200ForArticlesPageForCustomer() throws Exception {
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
                .andExpect(xpath("//*[@id='article']").nodeCount(8));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldForbidAccess() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_PAGE));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldSucceedWith200ForArticlesPageForSale() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldGetEightTestArticlesFromPage() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='article']").nodeCount(8));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveButtonNewArticle() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='newArticleButton']").nodeCount(1));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveButtonNewArticleForCustomer() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='newArticleButton']").nodeCount(0));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToNewArticlePageForSale() throws Exception {
        NewArticleDTO article = new NewArticleDTO();
        mockMvc.perform(get("/articles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("newArticle"));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToNewArticlePageForCustomer() throws Exception {
        mockMvc.perform(get("/articles/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_PAGE));
    }
}
