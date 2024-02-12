package com.richnachos.forum.controllers.post;

import com.richnachos.forum.controllers.post.requests.AddPostRequest;
import com.richnachos.forum.controllers.post.requests.IdRequest;
import com.richnachos.forum.controllers.post.responses.IdResponse;
import com.richnachos.forum.controllers.post.responses.PostResponse;
import com.richnachos.forum.controllers.post.responses.PostsResponse;
import com.richnachos.forum.entities.Post;
import com.richnachos.forum.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping("/posts/add")
    public ResponseEntity<IdResponse> addPost(@RequestBody AddPostRequest request) {
        Long id = service.savePost(request.getTitle(), request.getText());
        return ResponseEntity.ok(new IdResponse(id));
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsResponse> getAllPosts() {
        List<Post> posts = service.getPostsByOrderByUploadDateDesc();
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(new PostDTO(post));
        }
        PostsResponse response = new PostsResponse(postDTOS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponse> getPost(IdRequest request) {
        Post post = service.getPostById(request.getId());
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        PostResponse response = new PostResponse(new PostDTO(post));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/user/{id}")
    public ResponseEntity<PostsResponse> getPostsByUserId(IdRequest request) {
        List<Post> posts = service.getPostsByUserId(request.getId());
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(new PostDTO(post));
        }
        PostsResponse response = new PostsResponse(postDTOS);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(IdRequest request) {
        if (!service.deletePostById(request.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
}
