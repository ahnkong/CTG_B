package com.ctg.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctg.backend.dto.DomainDTO;
import com.ctg.backend.entity.Domain;
import com.ctg.backend.repository.DomainRepository;

@Service
public class DomainService {

    @Autowired
    private DomainRepository domainRepository;

    public DomainDTO getDomainById(Long domainId) {
        Domain domain = domainRepository.findById(domainId)
                .orElseThrow(() -> new IllegalArgumentException("Domain not found"));
        DomainDTO dto = new DomainDTO();
        dto.setDomainId(domain.getDomainId());
        dto.setDomainName(domain.getDomainName());
        dto.setLocation(domain.getLocation());
        dto.setMasterName(domain.getMasterName());
        dto.setContactNumber(domain.getContactNumber());
        dto.setEmail(domain.getEmail());
        dto.setCreatedAt(domain.getCreatedAt());
        dto.setUpdatedAt(domain.getUpdatedAt());
        dto.setIsActive(domain.getIsActive());
        dto.setUpperGroup(domain.getUpperGroup());
        dto.setLowerGroup(domain.getLowerGroup());
        dto.setLogoUrl(domain.getLogoUrl());
        dto.setHomepageUrl(domain.getHomepageUrl());
        dto.setDescription(domain.getDescription());
        dto.setYoutubeUrl(domain.getYoutubeUrl());
        return dto;
    }
}
