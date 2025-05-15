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
import com.ctg.backend.dto.SignUpRequestDTO;
import com.ctg.backend.entity.Role;
import com.ctg.backend.entity.User;
import com.ctg.backend.entity.Domain;
import com.ctg.backend.repository.UserRepository;
import com.ctg.backend.repository.DomainRepository;

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

    @Autowired
    private DomainRepository domainRepository;

    // private final String uploadDir = "/Users/jieunseo/uploads/profile";
    private final String uploadDir = "/Users/ahncoco/uploads/profile";
    // private final String uploadDir = "/Users/hylee/uploads/profile";

    // Key 객체 생성
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // JWT 생성
    public String generateToken(UserDTO user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24시간
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
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    // 사용자 저장 (회원가입)
    public UserDTO saveUser(SignUpRequestDTO signUpRequest) {
        if (existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (existsByNickname(signUpRequest.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        if (existsByTell(signUpRequest.getTell())) {
            throw new IllegalArgumentException("Tell already exists");
        }

        // 비밀번호 유효성 검사
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // 교회명으로 Domain 찾기 (없으면 null)
        Domain domain = null;
        if (signUpRequest.getChurchName() != null && !signUpRequest.getChurchName().isEmpty()) {
            domain = domainRepository.findByDomainName(signUpRequest.getChurchName()).orElse(null);
        }

        // DTO를 엔티티로 변환
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setName(signUpRequest.getName());
        user.setBirth(signUpRequest.getBirth());
        user.setNickname(signUpRequest.getNickname());
        user.setTell(signUpRequest.getTell());
        user.setIsActive(true);
        user.setRole(Role.USER);
        user.setLocal(signUpRequest.getLocal());
        user.setAgreeToTerms(signUpRequest.getAgreeToTerms());
        user.setAgreeToMarketing(signUpRequest.getAgreeToMarketing());
        user.setDomain(domain); // Domain 설정 (null 가능)
        user.setCreatedAt(LocalDateTime.now());

        // 저장
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    // 사용자 조회
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToDTO(user);
    }

    // 마이페이지 프로필 수정 후 업데이트
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 업데이트할 필드 설정
        if (userDTO.getNickname() != null)
            user.setNickname(userDTO.getNickname());
        if (userDTO.getInfo() != null)
            user.setInfo(userDTO.getInfo());
        if (userDTO.getTell() != null)
            user.setTell(userDTO.getTell());
        if (userDTO.getProfileImage() != null)
            user.setProfileImage(userDTO.getProfileImage());

        userRepository.save(user);
        return mapToDTO(user);
    }

    // 비밀번호 변경을 위한 사용자 정보 검증
    public boolean validateUserInfo(String name, String email) {
        return userRepository.findByNameAndEmail(name, email).isPresent();
    }

    // 이메일로 비밀번호 변경
    @Transactional
    public boolean updatePasswordByEmail(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return false;
        }
    
        User user = userOptional.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    // 사용자 ID와 비밀번호 검증
    public boolean validateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    // 이메일로 사용자 찾기
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToDTO(user);
    }

    // 프로필 사진 저장
    public String saveProfilePicture(Long userId, MultipartFile file) throws IOException {
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
    public void deleteProfilePicture(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 이미지 삭제
        deleteExistingProfileImage(user);

        // 사용자 데이터베이스 업데이트
        userRepository.save(user);
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
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setNickname(user.getNickname());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setInfo(user.getInfo());
        dto.setTell(user.getTell());
        dto.setProfileImage(user.getProfileImage());
        dto.setIsActive(user.getIsActive());
        dto.setLocal(user.getLocal());
        dto.setBirth(user.getBirth());
        dto.setAgreeToTerms(user.getAgreeToTerms());
        dto.setAgreeToMarketing(user.getAgreeToMarketing());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        // Domain 정보 설정
        if (user.getDomain() != null) {
            dto.setDomainId(user.getDomain().getDomainId());
            dto.setDomainName(user.getDomain().getDomainName());
        }
        
        return dto;
    }

    // 이메일 찾기
    public String findEmail(String name, String tell) {
        Optional<User> user = userRepository.findByNameAndTell(name, tell);
        return user.map(User::getEmail).orElse("일치하는 사용자가 없습니다.");
    }

    // 비밀번호 확인 메서드 (마이페이지내 프로필 수정 시 비밀번호 확인 구문)
    public boolean checkPassword(Long id, String password) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false; // 사용자 없음
        }
        return user.getPassword().equals(password); // 비밀번호 비교
    }

    // 자기소개 수정 메서드
    public boolean updateUserInfo(Long userId, String newInfo) {
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
    public boolean updateMarketing(Long userId, boolean marketing) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setAgreeToMarketing(marketing);
        userRepository.save(user);
        return marketing;
    }

    // 비밀번호 업데이트
    @Transactional
    public boolean updatePassword(Long userId, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isEmpty()) {
            return false;
        }
    
        User user = userOptional.get();
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email", String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }
}
