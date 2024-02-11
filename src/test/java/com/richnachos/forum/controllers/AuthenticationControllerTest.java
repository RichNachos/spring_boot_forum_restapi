package com.richnachos.forum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richnachos.forum.ForumApplication;
import com.richnachos.forum.controllers.authentication.http.auth.AuthenticationRequest;
import com.richnachos.forum.controllers.authentication.http.register.RegisterRequest;
import com.richnachos.forum.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ForumApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtests.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    private final String username = "user";
    private final String password = "password";

    @BeforeEach
    public void flushDatabase() {
        userRepository.deleteAll();
    }


    @Test
    public void registerUser() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .build();

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", startsWith("ey")));
    }

    @Test
    public void registerUserWithSameName() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .build();

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()));
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("")));

    }

    @Test
    public void authenticateUserWithCorrectPassword() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .build();

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", startsWith("ey")));

        AuthenticationRequest request1 = AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build();

        mvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", startsWith("ey")));
    }

    @Test
    public void authenticateUserWithWrongPassword() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username(username)
                .password(password)
                .build();

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.token", startsWith("ey")));

        AuthenticationRequest request1 = AuthenticationRequest.builder()
                .username(username)
                .password("wrong password")
                .build();

        mvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request1)))
                .andExpect(status().isForbidden());
    }
}
