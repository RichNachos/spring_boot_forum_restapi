package com.richnachos.forum.controllers.authentication;

import com.richnachos.forum.controllers.authentication.requests.AuthenticationRequest;
import com.richnachos.forum.controllers.authentication.responses.AuthenticationResponse;
import com.richnachos.forum.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        String token = service.register(request.getUsername(), request.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AuthenticationResponse response = new AuthenticationResponse(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        String token = service.authenticate(request.getUsername(), request.getPassword());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        AuthenticationResponse response = new AuthenticationResponse(token);
        return ResponseEntity.ok(response);
    }
}
