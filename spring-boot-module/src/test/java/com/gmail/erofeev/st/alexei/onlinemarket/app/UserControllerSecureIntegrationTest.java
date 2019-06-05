package com.gmail.erofeev.st.alexei.onlinemarket.app;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ADMIN_EMAIL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.CUSTOMER_EMAIL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.REDIRECT_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_ADMIN;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ROLE_CUSTOMER;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.SALE_EMAIL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.USER_DETAILS_SERVICE;
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
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = USER_DETAILS_SERVICE)
    public void shouldSucceedWith200ForUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(content().string(containsString("admin@gmail.com")));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = USER_DETAILS_SERVICE)
    public void shouldSucceedRedirectOnUsersPageAfterChangePassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setNewPassword("1234");
        passwordDTO.setOldPassword("admin");
        mockMvc.perform(post("/profile/changepassword").flashAttr("passwordDTO", passwordDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
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
    public void shouldGetFiveTestUserFromUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='user']").nodeCount(5));
    }

    @Test
    @WithMockUser(roles = ROLE_CUSTOMER)
    public void shouldForbidAccess() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(REDIRECT_URL));
    }

    @Test
    @WithMockUser(roles = ROLE_ADMIN)
    public void shouldSucceedWith200AfterAddUser() throws Exception {
        UserDTO user = new UserDTO(66L, "test", "test", "test", "test@gmail", new RoleDTO(10L, "Role_Customer"), false);
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

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, userDetailsServiceBeanName = USER_DETAILS_SERVICE)
    public void shouldSucceedWith200ForProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));

    }

    @Test
    @WithUserDetails(value = SALE_EMAIL, userDetailsServiceBeanName = USER_DETAILS_SERVICE)
    public void shouldSucceedWith200ForProfilePageForSale() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    @WithUserDetails(value = CUSTOMER_EMAIL, userDetailsServiceBeanName = USER_DETAILS_SERVICE)
    public void shouldSucceedWith200ForProfilePageForCustomer() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

}