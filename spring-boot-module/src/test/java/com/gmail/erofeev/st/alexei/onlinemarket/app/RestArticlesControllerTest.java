package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ArticleRestDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestArticlesControllerTest {
    private static final String ROLE_SECURE_REST_API = "SECURE_REST_API";
    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void getArticlesBySecureApi() throws Exception {
        String url = "/api/v1/articles";
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<ArticleRestDTO> articles = objectMapper.readValue(content, new TypeReference<List<ArticleRestDTO>>() {
        });
        Assert.assertEquals(4, articles.size());
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void getArticleById() throws Exception {
        String url = "/api/v1/articles/1";
        MvcResult mvcResult = mvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ArticleRestDTO articles = objectMapper.readValue(content, new TypeReference<ArticleRestDTO>() {
        });
        boolean isExist = false;
        if (articles.getId() == 1L) {
            isExist = true;
        }
        Assert.assertTrue(isExist);
    }

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void deleteArticleById() throws Exception {
        String url = "/api/v1/articles/1";
        MvcResult mvcResult = mvc.perform(delete(url))
                .andExpect(content().string("success"))
                .andExpect(status().isOk()).andReturn();
    }
}