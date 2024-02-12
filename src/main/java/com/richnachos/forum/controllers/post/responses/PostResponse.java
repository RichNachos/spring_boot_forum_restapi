package com.richnachos.forum.controllers.post.responses;

import com.richnachos.forum.controllers.post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private PostDTO post;
}
