package com.richnachos.forum.controllers;

import com.richnachos.forum.ForumApplication;
import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.PostRepository;
import com.richnachos.forum.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// This class tests all api endpoints of PostController
// Due to the nature of the business logic it is required that we also use AuthenticationController to get a token
// Otherwise we wouldn't be able to test certain (most) of the PostController endpoints.
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ForumApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtests.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final List<User> users = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>();
    private final String password = "password";

    @BeforeAll
    public void setUp() {
        String encrypted = passwordEncoder.encode(password);

        users.add(User.builder().username("user0").password(encrypted).role(Role.USER).build());
        users.add(User.builder().username("user1").password(encrypted).role(Role.USER).build());
        users.add(User.builder().username("user2").password(encrypted).role(Role.ADMIN).build());

        posts.add(Post.builder().title("title_0").text("text_0").poster(users.get(0)).build());
        posts.add(Post.builder().title("title_1").text("text_1").poster(users.get(0)).build());
        posts.add(Post.builder().title("title_2").text("text_2").poster(users.get(1)).build());
    }

    @BeforeEach
    public void flushDatabase() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getAllPostsShouldBeEmpty() throws Exception {
        mvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(0)));

    }

    @Test
    public void getAllPostsShouldBeSizeOne() throws Exception {
        addUser(0);
        addPost(0);

        mvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(1)));
    }

    @Test
    public void getAllPostsShouldBeSizeThree() throws Exception {
        addUser(0);
        addUser(1);
        addPost(0);
        addPost(1);
        addPost(2);
        mvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(3)));
    }

    @Test
    public void addWithoutTokenShouldBeForbidden() throws Exception {
        mvc.perform(post("/posts/add")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void postTitleShouldBeOne() throws Exception {
        addUser(0);
        addPost(0);
        mvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].title", is("title_0")));
    }

    @Test
    public void getPostById() throws Exception {
        addUser(0);
        addPost(0);

//        MvcResult result = mvc.perform(get("/posts")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.posts[0].id");

        Long id = postRepository.findAll().iterator().next().getId();
        mvc.perform(get("/posts/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.post.title", is("title_0")));
    }

    @Test
    public void getPostsByUserId() throws Exception {
        addUser(0);
        addPost(0);
        addPost(1);

//        MvcResult result = mvc.perform(get("/posts")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.posts[0].id");

        Long id = userRepository.findAll().iterator().next().getId();
        mvc.perform(get("/posts/user/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(2)))
                .andExpect(jsonPath("$.posts[0].posterUsername", is(users.get(0).getUsername())))
                .andExpect(jsonPath("$.posts[1].posterUsername", is(users.get(0).getUsername())));
    }

    // Get i-th users token
//    private String getToken(int i) throws Exception {
//        MvcResult result = mvc.perform(get("/authenticate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new StringBuilder()
//                                .append("{\"username\":\"user" + i + ",")
//                                .append("\"password:\"")
//                                .toString()))
//                .andExpect(status().isOk())
//                .andReturn();
//        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.posts[0].id");
//    }

    private void addUser(int i) {
        userRepository.save(users.get(i));
    }

    private void addPost(int i) {
        postRepository.save(posts.get(i));
    }
}