package com.richnachos.forum.controllers.post.http.getpost;

import com.richnachos.forum.controllers.post.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostResponse {
    private PostDTO post;
}
