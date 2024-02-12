package com.richnachos.forum.controllers.post.responses;

import com.richnachos.forum.controllers.post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponse {
    private List<PostDTO> posts;
}
