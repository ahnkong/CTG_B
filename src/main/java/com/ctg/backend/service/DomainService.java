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
        return dto;
    }
}
