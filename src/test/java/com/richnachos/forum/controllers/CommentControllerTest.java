package com.richnachos.forum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.richnachos.forum.ForumApplication;
import com.richnachos.forum.controllers.authentication.requests.AuthenticationRequest;
import com.richnachos.forum.controllers.comment.requests.AddCommentRequest;
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

    @Test
    public void getAllCommentsShouldBeSizeOne() throws Exception {
        addUser(0);
        addPost(0);
        addComment(0);
        Long id = postRepository.findAll().get(0).getId();
        mvc.perform(get("/posts/" + id + "/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(1)));
    }

    @Test
    public void getAllCommentsShouldBeSizeTwo() throws Exception {
        addUser(0);
        addUser(1);
        addPost(0);
        addComment(0);
        addComment(1);
        Long id = postRepository.findAll().get(0).getId();
        mvc.perform(get("/posts/" + id + "/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(2)));
    }

    @Test
    public void getCommentById() throws Exception {
        addUser(0);
        addUser(1);
        addPost(0);
        addComment(0);
        addComment(1);
        Comment comment = commentRepository.findAll().get(0);
        mvc.perform(get("/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment.text", is(comment.getText())));
    }

    @Test
    public void getNonexistentCommentById() throws Exception {
        addUser(0);
        addPost(0);
        mvc.perform(get("/comments/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getNonexistentPostComments() throws Exception {
        mvc.perform(get("/posts/0/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }


    @Test
    public void addComment() throws Exception {
        addUser(0);
        String token = authenticate(0);
        Long postId = addPost(0);
        AddCommentRequest request = new AddCommentRequest(comments.get(0).getText());
        MvcResult result = mvc.perform(post("/posts/" + postId + "/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Assertions.assertTrue(commentRepository.findById((long) id).isPresent());
        Assertions.assertEquals(commentRepository.findById((long) id).get().getText(), comments.get(0).getText());
    }

    @Test
    public void deleteComment() throws Exception {
        addUser(0);
        String token = authenticate(0);
        addPost(0);
        Long commentId = addComment(0);
        mvc.perform(delete("/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertTrue(commentRepository.findAll().isEmpty());
    }

    @Test
    public void addAndDeleteComment() throws Exception {
        addUser(0);
        String token = authenticate(0);
        Long postId = addPost(0);
        AddCommentRequest request = new AddCommentRequest(comments.get(0).getText());
        MvcResult result = mvc.perform(post("/posts/" + postId + "/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn();
        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Assertions.assertTrue(commentRepository.findById((long) id).isPresent());
        Assertions.assertEquals(commentRepository.findById((long) id).get().getText(), comments.get(0).getText());
        mvc.perform(delete("/comments/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
        Assertions.assertTrue(commentRepository.findAll().isEmpty());

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

    private Long addUser(int i) {
        return userRepository.save(users.get(i)).getId();
    }

    private Long addPost(int i) {
        return postRepository.save(posts.get(i)).getId();
    }

    private Long addComment(int i) {
        return commentRepository.save(comments.get(i)).getId();
    }
}