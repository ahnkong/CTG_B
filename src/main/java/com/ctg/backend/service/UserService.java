package com.ctg.backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ctg.backend.dto.UserDTO;
import com.ctg.backend.entity.Role;
import com.ctg.backend.entity.User;
import com.ctg.backend.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    // private final String uploadDir = "/Users/jieunseo/uploads/profile";
    private final String uploadDir = "/Users/ahncoco/uploads/profile";
    // private final String uploadDir = "/Users/hylee/uploads/profile";

    // Key 객체 생성
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // JWT 생성
    public String generateToken(UserDTO userDTO) {
        return Jwts.builder()
                .setSubject(userDTO.getUserId())
                .claim("nickname", userDTO.getNickname())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 유효
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // JWT에서 사용자 ID 추출
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 사용자 저장 (회원가입)
    public UserDTO saveUser(UserDTO userDTO) {
        if (existsById(userDTO.getUserId())) {
            throw new IllegalArgumentException("ID already exists");
        }
        if (existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (existsByNickname(userDTO.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        if (existsByTell(userDTO.getTell())) {
            throw new IllegalArgumentException("Tell already exists");
        }

        // 비밀번호 유효성 검사
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // DTO를 엔티티로 변환
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setNickname(userDTO.getNickname());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // 사용자 입력 비밀번호 설정
        user.setInfo(userDTO.getInfo());
        user.setChurchName(userDTO.getChurchName());
        user.setGrade(userDTO.getGrade());
        user.setTell(userDTO.getTell());
        user.setMarketing(userDTO.getMarketing());
        user.setProfileImage(userDTO.getProfileImage());
        user.setPoint(userDTO.getPoint() != null ? userDTO.getPoint() : 0L);
        user.setUDate(userDTO.getuDate() != null ? userDTO.getuDate() : LocalDateTime.now());

        // 기본값 처리
        // 소셜 타입에 따라 local 설정
        if ("KAKAO".equalsIgnoreCase(userDTO.getSocialType()) || "GOOGLE".equalsIgnoreCase(userDTO.getSocialType())) {
            user.setLocal(0); // 소셜 가입
        } else {
            user.setLocal(1); // 자체 가입
        }
        user.setRole(Role.USER); // 기본값 설정
        user.setIsActive(true);

        // 저장
        User savedUser = userRepository.save(user);

        // 엔티티 -> DTO 변환
        return mapToDTO(savedUser);
    }

    // 사용자 조회
    public UserDTO findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToDTO(user);
    }

    // 마이페이지 프로필 수정 후 업데이트
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 업데이트할 필드 설정
        if (userDTO.getNickname() != null)
            user.setNickname(userDTO.getNickname());
        if (userDTO.getInfo() != null)
            user.setInfo(userDTO.getInfo());
        if (userDTO.getPassword() != null)
            user.setPassword(userDTO.getPassword());
        if (userDTO.getTell() != null)
            user.setTell(userDTO.getTell());
        if (userDTO.getProfileImage() != null)
            user.setProfileImage(userDTO.getProfileImage());

        userRepository.save(user);
        return mapToDTO(user);
    }

    // 비밀번호 변경
    public boolean validateUserInfo(String name, String userId, String email) {
        return userRepository.findByUserIdAndNameAndEmail(userId, name, email).isPresent();
    }

    // 비밀번호 변경
    @Transactional
    public boolean updatePassword(String id, String newPassword) {
        Optional<User> userOptional = userRepository.findById(id);
        
        if (userOptional.isEmpty()) {
            System.out.println("❌ 비밀번호 변경 실패: 사용자 ID " + id + " 를 찾을 수 없음");
            return false; // 유저를 찾을 수 없음
        }
    
        User user = userOptional.get();
        System.out.println("✅ 기존 비밀번호: " + user.getPassword()); // 기존 비밀번호 확인
        user.setPassword(newPassword); // 🔥 새 비밀번호 설정
        userRepository.save(user); // 🔥 변경된 정보 저장
    
        System.out.println("✅ 비밀번호 변경 완료! 새로운 비밀번호: " + user.getPassword());
        return true;
    }
    

    // 비밀번호 변경
    // 비밀번호 변경 (암호화 X)
    // public boolean updatePassword(String id, String newPassword) {
    // Optional<User> user = userRepository.findById(id);
    // if (user.isPresent()) {
    // user.get().setPassword(newPassword); // 🔥 암호화 없이 그대로 저장
    // userRepository.save(user.get());
    // return true;
    // }
    // return false;
    // }

    // 사용자 ID와 비밀번호 검증
    public boolean validateUser(String userId, String password) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    // 프로필 사진 저장
    public String saveProfilePicture(String userId, MultipartFile file) throws IOException {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 이미지 삭제
        deleteExistingProfileImage(user);

        // 업로드 디렉토리 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
        }

        // 고유한 파일 이름 생성 및 저장
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, uniqueFileName);
        file.transferTo(filePath.toFile());

        // 사용자 객체에 프로필 이미지 경로 설정
        user.setProfileImage("/uploads/profile/" + uniqueFileName);

        // 변경된 사용자 정보 저장
        userRepository.save(user);

        // 저장된 프로필 이미지 경로 반환
        return user.getProfileImage();
    }

    // 기존 프로필 이미지 삭제
    private void deleteExistingProfileImage(User user) {
        if (user.getProfileImage() != null) {
            String relativePath = user.getProfileImage().replace("/uploads/profile/", "");
            File existingFile = new File(uploadDir, relativePath); // 파일 경로 생성
            if (existingFile.exists()) {
                existingFile.delete(); // 기존 파일 삭제
            }
            user.setProfileImage(null); // 데이터베이스에서 경로 초기화
        }
    }

    // 프로필 이미지 삭제
    public void deleteProfilePicture(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 이미지 삭제
        deleteExistingProfileImage(user);

        // 사용자 데이터베이스 업데이트
        userRepository.save(user);
    }

    // ID 중복 확인
    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

    // 이메일 중복 확인
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 닉네임 중복 확인
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 핸드폰 번호 중복 확인
    public boolean existsByTell(String tell) {
        return userRepository.existsByTell(tell);
    }

    // 엔티티 -> DTO 변환
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getNickname(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getChurchName(),
                user.getGrade(),
                user.getInfo(),
                user.getMarketing(),
                user.getTell(),
                user.getProfileImage(),
                user.getPoint(),
                user.getUDate(),
                user.getIsActive(),
                user.getPersonal(),
                null);
    }

    // 아이디 찾기
    public String findId(String name, String tell, String mail) {
        Optional<User> user = userRepository.findByNameAndTellAndEmail(name, tell, mail);
        return user.map(User::getUserId).orElse("일치하는 사용자가 없습니다.");
    }

    // 비밀번호 확인 메서드 (마이페이지내 프로필 수정 시 비밀번호 확인 구문)
    public boolean checkPassword(String id, String password) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false; // 사용자 없음
        }
        return user.getPassword().equals(password); // 비밀번호 비교
    }

    // 자기소개 수정 메서드
    public boolean updateUserInfo(String userId, String newInfo) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setInfo(newInfo); // 자기소개 업데이트
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // ✅ 마케팅 동의 여부 업데이트 메서드 추가
    @Transactional
    public boolean updateMarketing(String userId, boolean marketing) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userRepository.updateMarketing(userId, marketing);
        return marketing;
    }
}
