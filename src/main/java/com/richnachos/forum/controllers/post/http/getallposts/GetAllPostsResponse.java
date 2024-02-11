package com.richnachos.forum.controllers.post.http.getallposts;

import com.richnachos.forum.controllers.dtos.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPostsResponse {
    private List<PostDTO> posts;
}
