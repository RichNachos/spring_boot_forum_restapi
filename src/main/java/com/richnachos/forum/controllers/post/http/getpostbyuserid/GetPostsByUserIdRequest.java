package com.richnachos.forum.controllers.post.http.getpostbyuserid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPostsByUserIdRequest {
    private Long id;
}
