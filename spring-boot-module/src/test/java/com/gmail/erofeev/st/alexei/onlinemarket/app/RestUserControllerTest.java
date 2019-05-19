package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestUserControllerTest {
    private static final String ROLE_SECURE_REST_API = "SECURE_REST_API";
    @Autowired
    MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = ROLE_SECURE_REST_API)
    public void addNewUserByAdministrator() throws Exception {
        UserDTO user = new UserDTO();
        user.setEmail("spokeman152@yandex.ru");
        user.setLastName("test");
        user.setFirstName("testName");
        RoleDTO role = new RoleDTO();
        role.setName("ROLE_Customer");
        user.setRole(role);
        String json = objectMapper.writeValueAsString(user);
        String url = "/api/v1/users";
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
}
