package com.ctg.backend.config;

import com.ctg.backend.entity.BookUnified;
import com.ctg.backend.repository.BookUnifiedRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Component
public class CsvDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);

    @Value("classpath:books/웨스트민스터_신앙고백서.csv")
    private Resource csvResource;

    private final BookUnifiedRepository repository;

    public CsvDataLoader(BookUnifiedRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        try {
            if (!csvResource.exists()) {
                log.warn("CSV resource not found: {}. Skipping data load.", csvResource.getDescription());
                return;
            }
            try (Reader reader = new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8);
                 CSVReader csvReader = new CSVReader(reader)) {

                String[] header = csvReader.readNext(); // 헤더 건너뛰기
                String[] line;

                while ((line = csvReader.readNext()) != null) {
                    BookUnified entity = BookUnified.builder()
                            .title(line[0])
                            .author(line[1])
                            .isbn(line[2].isEmpty() ? null : line[2])
                            .publisher(line[3])
                            .chapterNumber(Integer.parseInt(line[4]))
                            .chapterTitle(line[5])
                            .sectionNumber(Integer.parseInt(line[6]))
                            .content(line[7])
                            .reference(line[8])
                            .build();

                    repository.save(entity);
                }
            }
        } catch (Exception e) {
            log.error("Failed to load CSV data", e);
        }
    }
}