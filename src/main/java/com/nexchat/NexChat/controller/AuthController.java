package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.request.authrequest.LoginRequest;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.security.JwtUtil;
import com.nexchat.NexChat.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService,
                          JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager
    ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> SingUp(@RequestBody User user) {
        try {
            if (user == null) {
                return ResponseEntity.ok("User is Null");
            }
            return ResponseEntity.ok(authService.signUp(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);


            String token = jwtUtil.generateToken(loginRequest.getUsername());


            return ResponseEntity.ok(token);


        } catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username of password");
        }
    }
}
