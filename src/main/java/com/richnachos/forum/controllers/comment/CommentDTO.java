package com.richnachos.forum.controllers.comment;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.richnachos.forum.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "comment")
public class CommentDTO {
    private Long id;
    private String text;
    private Long postId;
    private Long commenterId;
    private String commenterUsername;
    private Date uploadDate;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.postId = comment.getPost().getId();
        this.commenterId = comment.getCommenter().getId();
        this.commenterUsername = comment.getCommenter().getUsername();
        this.uploadDate = comment.getUploadDate();
    }
}
