package com.ctg.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctg.backend.dto.DomainDTO;
import com.ctg.backend.service.DomainService;

@RestController
@RequestMapping("/api/v1/domain")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @GetMapping("/{domainId}")
    public ResponseEntity<DomainDTO> getDomainById(@PathVariable Long domainId) {
        DomainDTO domain = domainService.getDomainById(domainId);
        return ResponseEntity.ok(domain);
    }
}
