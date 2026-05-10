package com.nexchat.NexChat.controller;

import com.nexchat.NexChat.modal.dto.OtpRequestDto;
import com.nexchat.NexChat.modal.dto.request.authrequest.LoginRequest;
import com.nexchat.NexChat.modal.dto.request.authrequest.RefreshTokenRequest;
import com.nexchat.NexChat.modal.dto.request.authrequest.SignupRequest;
import com.nexchat.NexChat.modal.dto.response.LoginResponse;
import com.nexchat.NexChat.modal.dto.response.TokenRefreshResponse;
import com.nexchat.NexChat.modal.entity.RefreshToken;
import com.nexchat.NexChat.modal.entity.User;
import com.nexchat.NexChat.repository.UserRepository;
import com.nexchat.NexChat.security.JwtUtil;
import com.nexchat.NexChat.service.AuthService;
import com.nexchat.NexChat.service.EmailService;
import com.nexchat.NexChat.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthController(UserRepository userRepository, AuthService authService,
                          JwtUtil jwtUtil, RefreshTokenService refreshTokenService,
                          AuthenticationManager authenticationManager, EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> SingUp(@Valid @RequestBody SignupRequest signupRequest) {

        authService.signUp(signupRequest);
        return ResponseEntity.ok("Registered Successfully!");

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);


            String token = jwtUtil.generateToken(loginRequest.getUsername());
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("user"));
            String refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername()).getToken();

            LoginResponse loginResponse = new LoginResponse(authentication.getName(), token,refreshToken, user.getId(), user.getEmail(), user.getCreatedAt(), user.getBio());

            return ResponseEntity.ok()
                    .body(loginResponse);


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username of password");
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody OtpRequestDto request) {

        emailService.sendOtpToEmail(request.getEmail());

        return ResponseEntity.ok("OTP sent to " + request.getEmail());

    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> varifyOtp(@Valid @RequestBody OtpRequestDto request) {

        String res = emailService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok().body(res);


    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));


        refreshTokenService.verifyExpiration(refreshToken);


        User user = refreshToken.getUser();
        if (user == null) {
            throw new RuntimeException("No user associated with this refresh token!");
        }

        String newAccessToken = jwtUtil.generateToken(user.getUsername());

        refreshTokenService.deleteByUserId(user.getId());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        TokenRefreshResponse response = new TokenRefreshResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );

        return ResponseEntity.ok(response);
    }
}
