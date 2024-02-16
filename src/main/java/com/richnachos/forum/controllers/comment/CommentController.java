package com.richnachos.forum.controllers.comment;

import com.richnachos.forum.controllers.comment.requests.AddCommentRequest;
import com.richnachos.forum.controllers.comment.responses.CommentResponse;
import com.richnachos.forum.controllers.comment.responses.CommentsResponse;
import com.richnachos.forum.controllers.comment.responses.IdResponse;
import com.richnachos.forum.entities.Comment;
import com.richnachos.forum.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;


    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentsResponse> getPostComments(
            @PathVariable Long postId
    ) {
        List<Comment> comments = service.getCommentsByPost(postId);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOS.add(new CommentDTO(comment));
        }
        CommentsResponse response = new CommentsResponse(commentDTOS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments")
    public ResponseEntity<CommentsResponse> getAllComments() {
        List<Comment> comments = service.getAllComments();
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOS.add(new CommentDTO(comment));
        }
        CommentsResponse response = new CommentsResponse(commentDTOS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getComment(
            @PathVariable Long commentId
    ) {
        Comment comment = service.getComment(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        CommentDTO commentDTO = new CommentDTO(comment);
        CommentResponse response = new CommentResponse(commentDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{postId}/comments/add")
    public ResponseEntity<IdResponse> addComment(
            @PathVariable Long postId,
            @RequestBody AddCommentRequest request
    ) {
        Long id = service.saveComment(request.getText(), postId);
        IdResponse response = new IdResponse(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        if (!service.deleteComment(commentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
}
