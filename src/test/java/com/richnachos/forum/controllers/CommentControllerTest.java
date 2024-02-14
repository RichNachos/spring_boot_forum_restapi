package com.richnachos.forum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.richnachos.forum.ForumApplication;
import com.richnachos.forum.controllers.authentication.requests.AuthenticationRequest;
import com.richnachos.forum.entities.Comment;
import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.CommentRepository;
import com.richnachos.forum.repositories.PostRepository;
import com.richnachos.forum.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final List<User> users = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();
    private final String password = "password";

    @BeforeAll
    public void setUp() {
        String encrypted = passwordEncoder.encode(password);

        users.add(User.builder().username("user0").password(encrypted).role(Role.USER).build());
        users.add(User.builder().username("user1").password(encrypted).role(Role.USER).build());
        users.add(User.builder().username("user2").password(encrypted).role(Role.ADMIN).build());

        posts.add(Post.builder().title("title_0").text("text_0").poster(users.get(0)).build());

        comments.add(Comment.builder().text("comment_0").post(posts.get(0)).commenter(users.get(0)).build());
        comments.add(Comment.builder().text("comment_1").post(posts.get(0)).commenter(users.get(1)).build());
    }

    @BeforeEach
    @AfterAll
    public void flushDatabase() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getAllCommentsShouldBeEmpty() throws Exception {
        addUser(0);
        addPost(0);
        Long id = postRepository.findAll().get(0).getId();
        mvc.perform(get("/posts/" + id + "/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    // Register i-th user and return token
    private String register(int i) throws Exception {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(users.get(i).getUsername())
                .password(password)
                .build();

        MvcResult result = mvc.perform(post("/auth/register")
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

    private void addPost(int i) {
        postRepository.save(posts.get(i));
    }

    private void addComment(int i) {
        commentRepository.save(comments.get(i));
    }
}