package com.doranco.site.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // You can set up test data here, if needed
    }

    @Test
    public void testLoginWithValidUser() throws Exception {
        mockMvc.perform(formLogin().user("john.doe@example.com").password("Password123!"))
                .andExpect(authenticated());
    }

    @Test
    public void testLoginWithInvalidUser() throws Exception {
        mockMvc.perform(formLogin().user("invalid@example.com").password("invalid"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "john.doe@example.com", roles = {"USER"})
    public void testAccessSecuredEndpointWithUser() throws Exception {
        mockMvc.perform(get("/secured-endpoint"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    public void testAccessAdminEndpointWithAdmin() throws Exception {
        mockMvc.perform(get("/admin-endpoint"))
                .andExpect(status().isOk());
    }
}
