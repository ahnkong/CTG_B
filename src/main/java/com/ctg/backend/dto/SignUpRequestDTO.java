package com.ctg.backend.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {
    private String email;
    private String password;
    private String name;
    private Date birth;
    private String nickname;
    private String tell;
    private String churchName;
    private Boolean agreeToTerms;
    private Boolean agreeToMarketing;
    private Integer local;
} 