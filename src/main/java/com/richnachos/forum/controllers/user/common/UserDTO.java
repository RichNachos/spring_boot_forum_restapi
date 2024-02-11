package com.richnachos.forum.controllers.user.common;

import com.richnachos.forum.entities.Role;
import com.richnachos.forum.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private Role role;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
