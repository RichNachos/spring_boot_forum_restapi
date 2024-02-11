package com.richnachos.forum.controllers.user.http.getuserbyid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByIdRequest {
    private Long id;
}
