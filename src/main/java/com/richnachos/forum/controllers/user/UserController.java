package com.richnachos.forum.controllers.user;

import com.richnachos.forum.controllers.user.http.deleteuserbyid.DeleteUserByIdRequest;
import com.richnachos.forum.controllers.user.http.deleteuserbyid.DeleteUserByIdResponse;
import com.richnachos.forum.controllers.user.http.demoteuserbyid.DemoteUserByIdRequest;
import com.richnachos.forum.controllers.user.http.demoteuserbyid.DemoteUserByIdResponse;
import com.richnachos.forum.controllers.user.http.getallusers.GetAllUsersResponse;
import com.richnachos.forum.controllers.user.http.getuserbyid.GetUserByIdRequest;
import com.richnachos.forum.controllers.user.http.getuserbyid.GetUserByIdResponse;
import com.richnachos.forum.controllers.user.http.getuserbyusername.GetUserByUsernameRequest;
import com.richnachos.forum.controllers.user.http.getuserbyusername.GetUserByUsernameResponse;
import com.richnachos.forum.controllers.user.http.promoteuserbyid.PromoteUserByIdRequest;
import com.richnachos.forum.controllers.user.http.promoteuserbyid.PromoteUserByIdResponse;
import com.richnachos.forum.entities.User;
import com.richnachos.forum.services.UserService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(new UserDTO(user));
        }
        GetAllUsersResponse response = new GetAllUsersResponse(userDTOS);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/id={id}")
    public ResponseEntity<GetUserByIdResponse> getUserById(GetUserByIdRequest request) {
        UserDTO userDTO = new UserDTO(userService.getUserById(request.getId()));
        GetUserByIdResponse response = new GetUserByIdResponse(userDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/username={username}")
    public ResponseEntity<GetUserByUsernameResponse> getUserByUsername(GetUserByUsernameRequest request) {
        UserDTO userDTO = new UserDTO(userService.getUserByUsername(request.getUsername()));
        GetUserByUsernameResponse response = new GetUserByUsernameResponse(userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/promote")
    public ResponseEntity<PromoteUserByIdResponse> promoteUserById(PromoteUserByIdRequest request) {
        PromoteUserByIdResponse response = new PromoteUserByIdResponse(userService.promoteUserById(request.getId()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/demote")
    public ResponseEntity<DemoteUserByIdResponse> DemoteUserById(DemoteUserByIdRequest request) {
        DemoteUserByIdResponse response = new DemoteUserByIdResponse(userService.demoteUserById(request.getId()));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<DeleteUserByIdResponse> deleteUserById(DeleteUserByIdRequest request) {
        DeleteUserByIdResponse response = new DeleteUserByIdResponse(userService.deleteUserById(request.getId()));
        return ResponseEntity.ok(response);
    }
}
