package com.richnachos.forum.controllers.comment.responses;

import com.richnachos.forum.controllers.comment.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponse {
    private List<CommentDTO> comments;
}
