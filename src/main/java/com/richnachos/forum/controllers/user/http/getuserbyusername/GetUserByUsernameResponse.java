package com.richnachos.forum.controllers.user.http.getuserbyusername;

import com.richnachos.forum.controllers.user.common.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserByUsernameResponse {
    private UserDTO userDTO;
}
