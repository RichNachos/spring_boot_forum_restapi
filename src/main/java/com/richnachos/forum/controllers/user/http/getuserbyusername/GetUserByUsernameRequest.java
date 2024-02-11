package com.richnachos.forum.controllers.user.http.getuserbyusername;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByUsernameRequest {
    private String username;
}
