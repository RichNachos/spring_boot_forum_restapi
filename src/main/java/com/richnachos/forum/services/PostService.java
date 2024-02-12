package com.richnachos.forum.services;

import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.PostRepository;
import com.richnachos.forum.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return postRepository.findPostsByPoster(user);
    }

    public List<Post> getPostsByOrderByUploadDateDesc() {
        return postRepository.findAllByOrderByUploadDateDesc();
    }
    
    public Long savePost(String title, String text) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Post post = Post.builder()
                .poster(user)
                .title(title)
                .text(text)
                .build();
        return postRepository.save(post).getId();
    }

    public void updatePost(Post post) {
        postRepository.save(post);
    }

    public boolean deletePostById(Long id) {
        if (getPostById(id) == null) {
            return false;
        }
        Post post = getPostById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getRole());
        if (post.getPoster().equals(user) || user.getRole() == Role.ADMIN) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    public List<Post> findAllPosts() {
        return (List<Post>) postRepository.findAll();
    }

}
