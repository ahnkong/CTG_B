package com.ctg.backend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ctg.backend.dto.LoginRequest;
import com.ctg.backend.dto.SignUpRequestDTO;
import com.ctg.backend.dto.UserDTO;
import com.ctg.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class AuthController {
    
    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody SignUpRequestDTO signUpRequest) {
        UserDTO savedUser = userService.saveUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            boolean isAuthenticated = userService.validateUser(loginRequest.getEmail(), loginRequest.getPassword());
            if (isAuthenticated) {
                UserDTO user = userService.findByEmail(loginRequest.getEmail());

                // JWT 생성
                String token = userService.generateToken(user);

                // HttpOnly 쿠키에 JWT 저장
                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24);
                response.addCookie(cookie);

                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "email", user.getEmail(),
                        "nickname", user.getNickname(),
                        "userId", user.getUserId()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // 토큰 인증 확인
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        if (!userService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String email = userService.getEmailFromToken(token);
        UserDTO user = userService.findByEmail(email);
        return ResponseEntity.ok(Map.of(
            "email", email,
            "userId", user.getUserId()
        ));
    }

    // 비밀번호 재설정
    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String name = request.get("name");
        String newPassword = request.get("newPassword");

        boolean isValid = userService.validateUserInfo(name, email);
        Map<String, String> response = new HashMap<>();
        if (isValid) {
            boolean isUpdated = userService.updatePasswordByEmail(email, newPassword);
            if (isUpdated) {
                response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            } else {
                response.put("message", "비밀번호 변경에 실패했습니다.");
            }
        } else {
            response.put("message", "사용자 정보가 일치하지 않습니다.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkEmail")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(!exists); // true: 사용 가능, false: 사용 중
    }

    @GetMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);
        return ResponseEntity.ok(!exists); // true: 사용 가능, false: 사용 중
    }

    @GetMapping("/checkTell")
    public ResponseEntity<Boolean> checkTell(@RequestParam String tell) {
        boolean exists = userService.existsByTell(tell);
        return ResponseEntity.ok(!exists); // true: 사용 가능, false: 사용 중
    }
}
