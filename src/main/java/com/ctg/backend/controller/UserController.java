package com.ctg.backend.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.UserDTO;
import com.ctg.backend.service.UserService;


@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class UserController {
    
    @Autowired
    private UserService userService;

    // 사용자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    // 사용자 정보 업데이트
    @PutMapping("/{id}/update")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 프로필 사진 업로드
    @PostMapping("/{id}/uploadProfileImage")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        try {
            String profileImageUrl = userService.saveProfilePicture(id, file);
            return ResponseEntity.ok(Map.of("profileImageUrl", profileImageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "파일 저장 실패"));
        }
    }

    // 프로필 사진 삭제
    @DeleteMapping("/{userId}/deleteProfileImage")
    public ResponseEntity<String> deleteProfileImage(@PathVariable String userId) {
        try {
            userService.deleteProfilePicture(userId);
            return ResponseEntity.ok("프로필 이미지가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 아이디 찾기
    @PostMapping("/findId")
    public ResponseEntity<Map<String, String>> findId(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String mail) {
        String userId = userService.findId(name, phone, mail);
        return ResponseEntity.ok(Map.of("userId", userId));
    }

    // 비밀번호 확인
    @PostMapping("/{id}/checkPassword")
    public ResponseEntity<Map<String, String>> checkPassword(
            @PathVariable String id, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        boolean isPasswordValid = userService.checkPassword(id, password);
        return ResponseEntity.ok(Map.of("isValid", String.valueOf(isPasswordValid)));
    }


    @PostMapping("/{userId}/updatePassword")
    public ResponseEntity<Void> updatePassword(@PathVariable String userId, @RequestBody Map<String, String> request) {
        String newPassword = request.get("password");  // ✅ 프론트에서 보낸 새 비밀번호
    
        if (newPassword == null || newPassword.length() < 8) {
            return ResponseEntity.badRequest().build(); // 비밀번호 8자 이상 체크
        }
    
        boolean isUpdated = userService.updatePassword(userId, newPassword);
    
        if (!isUpdated) {
            return ResponseEntity.notFound().build(); // 유저 못 찾으면 404 응답
        }
    
        return ResponseEntity.ok().build(); // 성공 시 200 응답 (메시지 없음)
    }
    
    

    // 자기소개 수정
    @PutMapping("/{id}/updateInfo")
    public ResponseEntity<Map<String, String>> updateUserInfo(
            @PathVariable String id, @RequestBody Map<String, String> request) {
        String newInfo = request.get("info");
        boolean isUpdated = userService.updateUserInfo(id, newInfo);
        if (isUpdated) {
            return ResponseEntity.ok(Map.of("message", "자기소개가 성공적으로 업데이트되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "사용자를 찾을 수 없습니다."));
        }
    }

    //마케팅 동의 활용 수정
    //사용자 정보 업데이트 (마케팅 동의 포함)
    @PutMapping("/{id}/updateMarketing")
    public ResponseEntity<Boolean> updateMarketing(@PathVariable String id, @RequestBody Map<String, Boolean> request) {
        boolean marketingConsent = request.get("marketing");
        boolean updatedMarketing = userService.updateMarketing(id, marketingConsent);
        return ResponseEntity.ok(updatedMarketing);
    }
    
    
}
