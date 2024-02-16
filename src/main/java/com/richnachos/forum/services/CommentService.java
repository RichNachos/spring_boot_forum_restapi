package com.richnachos.forum.services;

import com.richnachos.forum.entities.Comment;
import com.richnachos.forum.entities.Post;
import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.repositories.CommentRepository;
import com.richnachos.forum.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        return commentRepository.findAllByPost(post);
    }

    public void saveComment(String text, Long postId) {
        Comment comment = Comment.builder()
                .commenter(getAuthenticatedUser())
                .post(postRepository.findById(postId).orElse(null))
                .text(text)
                .build();
        commentRepository.save(comment);
    }

    public boolean deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return false;
        }
        if (!comment.getCommenter().equals(getAuthenticatedUser()) && getAuthenticatedUser().getRole() != Role.ADMIN) {
            return false;
        }
        commentRepository.deleteById(id);
        return true;
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
