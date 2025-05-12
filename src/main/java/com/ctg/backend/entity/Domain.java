package com.ctg.backend.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Domain")
@Getter
@Setter
public class Domain {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_id")
    private Long domainId;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "location")
    private String location;

    @Column(name = "master_name")
    private String masterName;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "upper_group")
    private String upperGroup = "장로교";

    @Column(name = "lower_group")
    private String lowerGroup = "합동신학";

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "homepage_url")
    private String homepageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "domain")
    private List<User> users;
}
