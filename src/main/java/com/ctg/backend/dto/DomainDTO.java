package com.ctg.backend.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainDTO {
    private Long domainId;
    private String domainName;
    private String location;
    private String masterName;
    private String contactNumber;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String upperGroup;
    private String lowerGroup;
    private String logoUrl;
    private String homepageUrl;
    private String description;

    @Builder
    public void DomainResponseDTO(Long domainId, String domainName, String location, String masterName,
                             String contactNumber, String email, LocalDateTime createdAt, LocalDateTime updatedAt,
                             Boolean isActive, String upperGroup, String lowerGroup,
                             String logoUrl, String homepageUrl, String description) {
        this.domainId = domainId;
        this.domainName = domainName;
        this.location = location;
        this.masterName = masterName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.upperGroup = upperGroup;
        this.lowerGroup = lowerGroup;
        this.logoUrl = logoUrl;
        this.homepageUrl = homepageUrl;
        this.description = description;
    }
}
