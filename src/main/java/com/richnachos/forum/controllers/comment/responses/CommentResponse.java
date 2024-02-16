package com.richnachos.forum.controllers.comment.responses;

import com.richnachos.forum.controllers.comment.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private CommentDTO comment;
}
