package com.richnachos.forum.controllers.post.http.getpostbyuserid;

import com.richnachos.forum.controllers.post.common.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsByUserIdResponse {
    private List<PostDTO> postDTOS;
}
