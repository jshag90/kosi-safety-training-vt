package com.kosi.kosi_safety_training_vt.controller;

import com.kosi.kosi_safety_training_vt.service.CourseLectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/videos")
public class LectureController {

    private final CourseLectureService courseLectureService;

    @GetMapping("/lecture/{lectureId}")
    public Mono<ResponseEntity<ResourceRegion>> getLectureStream(
            @PathVariable Long lectureId,
            @RequestHeader HttpHeaders headers
    ) {

        log.info("LectureStream 요청 시작. lectureId={}, thread={}", lectureId, Thread.currentThread().getName());
        return courseLectureService.getLectureStream(headers, lectureId)
                .doOnNext(dto -> log.info("LectureStreamDto 준비 완료. lectureId={}, thread={}", lectureId, Thread.currentThread().getName()))
                .doOnError(ex -> log.error("LectureStream 처리 중 오류 발생. lectureId={}, error={}", lectureId, ex.getMessage(), ex))
                .map(dto -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .contentType(dto.getMediaType())
                        .body(dto.getResourceRegion()));
    }

}
