package com.ctg.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ctg.backend.entity.MBTI;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String nickname;
    private String name;
    private String email;
    private String password;
    private String churchName;
    private String grade;
    private String info;
    private Boolean marketing;
    private String tell;
    private String profileImage;
    private Long point;
    private LocalDateTime uDate;
    private Boolean isActive;
    private MBTI mbti;
    private String personal;
    private String socialType; // 소셜 타입 추가 (local, kakao, Google)
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getChurchName() {
        return churchName;
    }
    public void setChurchName(String churchName) {
        this.churchName = churchName;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public Boolean getMarketing() {
        return marketing;
    }
    public void setMarketing(Boolean marketing) {
        this.marketing = marketing;
    }
    public String getTell() {
        return tell;
    }
    public void setTell(String tell) {
        this.tell = tell;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public Long getPoint() {
        return point;
    }
    public void setPoint(Long point) {
        this.point = point;
    }
    public LocalDateTime getuDate() {
        return uDate;
    }
    public void setuDate(LocalDateTime uDate) {
        this.uDate = uDate;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public MBTI getMbti() {
        return mbti;
    }
    public void setMbti(MBTI mbti) {
        this.mbti = mbti;
    }
    public String getPersonal() {
        return personal;
    }
    public void setPersonal(String personal) {
        this.personal = personal;
    }
    public String getSocialType() {
        return socialType;
    }
    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    
    
    
    
    
    
    

}
