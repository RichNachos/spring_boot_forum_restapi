package com.richnachos.forum.controllers.post;

import com.richnachos.forum.controllers.post.http.deletepost.DeletePostRequest;
import com.richnachos.forum.controllers.post.http.deletepost.DeletePostResponse;
import com.richnachos.forum.controllers.post.http.getallposts.GetAllPostsResponse;
import com.richnachos.forum.controllers.post.http.getpost.GetPostRequest;
import com.richnachos.forum.controllers.post.http.getpost.GetPostResponse;
import com.richnachos.forum.controllers.post.http.getpostbyuserid.GetPostsByUserIdRequest;
import com.richnachos.forum.controllers.post.http.getpostbyuserid.GetPostsByUserIdResponse;
import com.richnachos.forum.controllers.post.http.newpost.AddPostRequest;
import com.richnachos.forum.controllers.post.http.newpost.AddPostResponse;
import com.richnachos.forum.entities.Post;
import com.richnachos.forum.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    @PostMapping("/posts/add")
    public ResponseEntity<AddPostResponse> addPost(@RequestBody AddPostRequest request) {
        Long id = service.savePost(request.getTitle(), request.getText());
        return ResponseEntity.ok(new AddPostResponse(id));
    }

    @GetMapping("/posts")
    public ResponseEntity<GetAllPostsResponse> getAllPosts() {
        List<Post> posts = service.getPostsByOrderByUploadDateDesc();
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(new PostDTO(post));
        }
        GetAllPostsResponse response = new GetAllPostsResponse(postDTOS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<GetPostResponse> getPost(GetPostRequest request) {
        Post post = service.getPostById(request.getId());
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        GetPostResponse response = new GetPostResponse(new PostDTO(post));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/user/{id}")
    public ResponseEntity<GetPostsByUserIdResponse> getPostsByUserId(GetPostsByUserIdRequest request) {
        List<Post> posts = service.getPostsByUserId(request.getId());
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Post post : posts) {
            postDTOS.add(new PostDTO(post));
        }
        GetPostsByUserIdResponse response = new GetPostsByUserIdResponse(postDTOS);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<DeletePostResponse> deletePost(DeletePostRequest request) {
        boolean deleted = service.deletePostById(request.getId());
        return ResponseEntity.ok(new DeletePostResponse(deleted));
    }
}
