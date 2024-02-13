package com.richnachos.forum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.richnachos.forum.ForumApplication;
import com.richnachos.forum.controllers.authentication.requests.AuthenticationRequest;
import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    private final String password = "password";
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final List<User> users = new ArrayList<>();

    @BeforeAll
    public void setUp() {
        String encrypted = passwordEncoder.encode(password);

        users.add(User.builder().username("user0").password(encrypted).role(Role.USER).build());
        users.add(User.builder().username("user1").password(encrypted).role(Role.ADMIN).build());
        users.add(User.builder().username("user2").password(encrypted).role(Role.ADMIN).build());
    }

    @BeforeEach
    public void flushDatabase() {
        userRepository.deleteAll();
    }


    @Test
    public void getAllPostsShouldBeEmpty() throws Exception {
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(0)));

    }

    @Test
    public void getAllPostsShouldBeSizeOne() throws Exception {
        addUser(0);

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)));
    }

    @Test
    public void getAllPostsShouldBeSizeThree() throws Exception {
        addUser(0);
        addUser(1);
        addUser(2);
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(3)));
    }

    @Test
    public void getUserWithId() throws Exception {
        addUser(0);
        Long id = userRepository.findAll().iterator().next().getId();
        mvc.perform(get("/users/id=" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is(users.get(0).getUsername())));
    }

    @Test
    public void getUserWithUsername() throws Exception {
        addUser(0);
        String username = userRepository.findAll().iterator().next().getUsername();
        mvc.perform(get("/users/username=" + username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is(users.get(0).getUsername())));
    }

    @Test
    public void getUserThatDoesntExist() throws Exception {
        mvc.perform(get("/users/username=user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        mvc.perform(get("/users/id=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void promoteUser() throws Exception {
        addUser(0);
        addUser(2);
        String token = authenticate(2);
        Long id = userRepository.findUserByUsername(users.get(0).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertEquals(Role.ADMIN, userRepository.findUserByUsername(users.get(0).getUsername()).getRole());
    }

    @Test
    public void demoteAdmin() throws Exception {
        addUser(1);
        addUser(2);
        String token = authenticate(2);
        Long id = userRepository.findUserByUsername(users.get(1).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/demote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertEquals(Role.USER, userRepository.findUserByUsername(users.get(1).getUsername()).getRole());
    }

    @Test
    public void promoteAndDemoteUser() throws Exception {
        addUser(0);
        addUser(2);
        String token = authenticate(2);
        Long id = userRepository.findUserByUsername(users.get(0).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertEquals(Role.ADMIN, userRepository.findUserByUsername(users.get(0).getUsername()).getRole());
        mvc.perform(post("/users/" + id + "/demote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertEquals(Role.USER, userRepository.findUserByUsername(users.get(0).getUsername()).getRole());
    }

    @Test
    public void promoteAdmin() throws Exception {
        addUser(1);
        addUser(2);
        String token = authenticate(2);
        Long id = userRepository.findUserByUsername(users.get(1).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void demoteUser() throws Exception {
        addUser(0);
        addUser(2);
        String token = authenticate(2);
        Long id = userRepository.findUserByUsername(users.get(0).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/demote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void promoteAndDemoteAsUser() throws Exception {
        addUser(0);
        addUser(1);
        addUser(2);
        String token = authenticate(0);
        Long id = userRepository.findUserByUsername(users.get(0).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
        id = userRepository.findUserByUsername(users.get(1).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/demote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
        id = userRepository.findUserByUsername(users.get(1).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
        id = userRepository.findUserByUsername(users.get(0).getUsername()).getId();
        mvc.perform(post("/users/" + id + "/demote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUser() throws Exception {
        addUser(0);
        addUser(2);
        String token = authenticate(2);
        Long id = userRepository.findUserByUsername(users.get(0).getUsername()).getId();
        mvc.perform(delete("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertNull(userRepository.findUserByUsername(users.get(0).getUsername()));
    }

    @Test
    public void deleteNonexistentUser() throws Exception {
        addUser(2);
        String token = authenticate(2);
        mvc.perform(delete("/users/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserAsUser() throws Exception {
        addUser(0);
        addUser(1);
        String token = authenticate(0);
        Long id = userRepository.findUserByUsername(users.get(1).getUsername()).getId();
        mvc.perform(delete("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
        Assertions.assertNotNull(userRepository.findUserByUsername(users.get(1).getUsername()));
    }

    @Test
    public void deleteNonexistentUserAsUser() throws Exception {
        addUser(0);
        String token = authenticate(0);
        mvc.perform(delete("/users/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isForbidden());
    }

    // Get i-th user's token
    private String authenticate(int i) throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(users.get(i).getUsername())
                .password(password)
                .build();

        MvcResult result = mvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", startsWith("ey")))
                .andReturn();

        return "Bearer " + JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    private void addUser(int i) {
        userRepository.save(users.get(i));
    }

}
