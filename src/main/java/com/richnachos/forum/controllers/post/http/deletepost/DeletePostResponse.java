package com.richnachos.forum.controllers.post.http.deletepost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletePostResponse {
    private boolean deleted;
}
