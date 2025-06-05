package com.ctg.backend.controller;

import com.ctg.backend.entity.BookUnified;
import com.ctg.backend.repository.BookUnifiedRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")  // React 개발 서버에서의 요청 허용
public class BookUnifiedController {

    private final BookUnifiedRepository repository;

    public BookUnifiedController(BookUnifiedRepository repository) {
        this.repository = repository;
    }

    /**
     * 전체 신앙고백서 항목 조회
     * GET /api/books
     */
    @GetMapping
    public List<BookUnified> getAllEntries() {
        return repository.findAll();
    }

    /**
     * 특정 챕터의 모든 절(Section) 조회
     * GET /api/books/chapter/{chapter}
     */
    @GetMapping("/chapter/{chapter}")
    public List<BookUnified> getEntriesByChapter(
            @PathVariable("chapter") Integer chapter
    ) {
        return repository.findAllByChapterNumberOrderBySectionNumber(chapter);
    }

    /**
     * 단일 항목(장+절) 조회
     * GET /api/books/{chapter}/{section}
     */
    @GetMapping("/{chapter}/{section}")
    public ResponseEntity<BookUnified> getEntryByChapterAndSection(
            @PathVariable("chapter") Integer chapter,
            @PathVariable("section") Integer section
    ) {
        Optional<BookUnified> entry =
                repository.findByChapterNumberAndSectionNumber(chapter, section);

        return entry
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
