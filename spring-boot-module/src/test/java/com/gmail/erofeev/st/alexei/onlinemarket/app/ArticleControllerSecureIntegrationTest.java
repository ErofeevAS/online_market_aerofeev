package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.CommentDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleControllerSecureIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldSucceedWith200ForArticlesPageForCustomer() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"))
                .andExpect(content().string(containsString("user@gmail.com")));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldSucceedRedirectOnArticlePage() throws Exception {
        mockMvc.perform(get("/articles/1"))
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
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToArticleWithTagPageForAdmin() throws Exception {
        mockMvc.perform(get("/articles/tag/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToArticleWithTagPageForCustomer() throws Exception {
        mockMvc.perform(get("/articles/tag/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldNotHaveAccessToArticleWithTagPageForSale() throws Exception {
        mockMvc.perform(get("/articles/tag/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"));
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
        mockMvc.perform(get("/articles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("newArticle"));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToNewArticlePageForSalePost() throws Exception {
        mockMvc.perform(post("/articles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("newArticle"));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToNewArticlePageForCustomer() throws Exception {
        mockMvc.perform(get("/articles/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToNewArticlePageForCustomerPost() throws Exception {
        mockMvc.perform(post("/articles/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToNewArticlePageForAdmin() throws Exception {
        mockMvc.perform(get("/articles/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToNewArticlePageForAdminPost() throws Exception {
        mockMvc.perform(post("/articles/new"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToUpdateArticlePageForSale() throws Exception {
        mockMvc.perform(post("/articles/1/update"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles/1"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToUpdateArticlePageForAdmin() throws Exception {
        mockMvc.perform(post("/articles/1/update"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToUpdateArticlePageForCustomer() throws Exception {
        mockMvc.perform(post("/articles/1/update"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_SALE)
    public void shouldHaveAccessToDeleteCommentInArticleForSale() throws Exception {
        mockMvc.perform(post("/articles/1/deleteComment")
                .param("deleteCommentId", "2"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles/1"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldNotHaveAccessToDeleteCommentInArticleForAdmin() throws Exception {
        mockMvc.perform(post("/articles/1/deleteComment"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldNotHaveAccessToDeleteCommentInArticleForCustomer() throws Exception {
        mockMvc.perform(post("/articles/1/deleteComment"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithUserDetails(SALE_EMAIL)
    public void shouldHaveAccessToCreateNewCommentInArticleForSale() throws Exception {
        CommentDTO comment = new CommentDTO();
        comment.setArticleId(1L);
        comment.setContent("test");
        mockMvc.perform(post("/articles/1/newComment")
                .flashAttr("newComment", comment))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/articles/1"));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    public void shouldNotHaveAccessToCreateNewCommentInArticleForAdmin() throws Exception {
        mockMvc.perform(post("/articles/1/newComment"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithUserDetails(CUSTOMER_EMAIL)
    public void shouldHaveAccessToCreateNewCommentInArticleForCustomer() throws Exception {
        CommentDTO comment = new CommentDTO();
        comment.setArticleId(1L);
        comment.setContent("test");
        mockMvc.perform(post("/articles/1/newComment")
                .flashAttr("newComment", comment))
                .andExpect(redirectedUrl("/articles/1"));
    }
}
