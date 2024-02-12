package com.richnachos.forum.controllers.user.responses;

import com.richnachos.forum.controllers.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UserDTO user;
}
