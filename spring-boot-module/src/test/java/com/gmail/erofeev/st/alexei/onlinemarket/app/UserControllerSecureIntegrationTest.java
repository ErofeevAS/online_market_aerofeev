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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerSecureIntegrationTest {
    private static final String ROLE_ADMIN = "Administrator";
    private static final String ROLE_CUSTOMER = "Customer";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedWith200ForUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(content().string(containsString("admin@gmail.com")));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedRedirectOnUsersPageAfterChangePassword() throws Exception {
        mockMvc.perform(post("/users/changepassword").param("userId", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedWith200ForUserAddPage() throws Exception {
        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("adduser"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedWith200ForUserAddPagePost() throws Exception {
        mockMvc.perform(post("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("adduser"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldGetThreeTestUserFromUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='user']").nodeCount(4));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldForbidAccess() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/403"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedWith200AfterAddUser() throws Exception {
        UserDTO user = new UserDTO(66L, "test", "test", "test", "test@gmail", new RoleDTO(10L, "Role_Customer2"), false);
        mockMvc.perform(post("/users/add").requestAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("adduser"));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedRedirectOnUsersPageAfterDeleteUser() throws Exception {
        mockMvc.perform(post("/users/delete").param("deletedUsersId", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/users"));
    }
}