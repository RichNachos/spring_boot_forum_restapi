package com.richnachos.forum.controllers.user;

import com.richnachos.forum.controllers.user.requests.IdRequest;
import com.richnachos.forum.controllers.user.requests.UsernameRequest;
import com.richnachos.forum.controllers.user.responses.UserResponse;
import com.richnachos.forum.controllers.user.responses.UsersResponse;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UsersResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(new UserDTO(user));
        }
        UsersResponse response = new UsersResponse(userDTOS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/id={id}")
    public ResponseEntity<UserResponse> getUserById(IdRequest request) {
        User user = userService.getUserById(request.getId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = new UserDTO(user);
        UserResponse response = new UserResponse(userDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/username={username}")
    public ResponseEntity<UserResponse> getUserByUsername(UsernameRequest request) {
        User user = userService.getUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse response = new UserResponse(new UserDTO(user));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/promote")
    public ResponseEntity<Void> promoteUserById(IdRequest request) {
        if (!userService.promoteUserById(request.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/demote")
    public ResponseEntity<Void> demoteUserById(IdRequest request) {
        if (!userService.demoteUserById(request.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(IdRequest request) {
        if (!userService.deleteUserById(request.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
}
