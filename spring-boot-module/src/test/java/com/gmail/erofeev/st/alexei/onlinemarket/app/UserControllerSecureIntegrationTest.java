package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
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
public class UserControllerSecureIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedWith200ForUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(content().string(containsString("admin@gmail.com")));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedRedirectOnUsersPageAfterChangePassword() throws Exception {
        mockMvc.perform(post("/users/changepassword").param("idForPasswordChange", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedWith200ForUserAddPage() throws Exception {
        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("adduser"));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedWith200ForUserAddPagePost() throws Exception {
        mockMvc.perform(post("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("adduser"));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldGetThreeTestUserFromUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='user']").nodeCount(3));
    }

    @Test
    @WithMockUser(roles = "Customer")
    public void shouldForbidAccess() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedWith200AfterAddUser() throws Exception {
        UserDTO user = new UserDTO(66L, "test", "test", "test", "test@gmail", new RoleDTO(10L, "Role_Customer2"), false);
        mockMvc.perform(post("/users/add").requestAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("adduser"));
    }

    @Test
    @WithMockUser(roles = "Administrator")
    public void shouldSucceedRedirectOnUsersPageAfterDeleteUser() throws Exception {
        mockMvc.perform(post("/users/delete").param("deletedUsersId", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }
}