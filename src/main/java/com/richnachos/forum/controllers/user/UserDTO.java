package com.richnachos.forum.controllers.user;

import com.fasterxml.jackson.annotation.JsonRootName;
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
@JsonRootName("user")
public class UserDTO {
    private Long id;
    private String username;
    private Role role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
