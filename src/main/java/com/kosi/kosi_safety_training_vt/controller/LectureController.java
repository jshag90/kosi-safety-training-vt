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
public class LectureController {

    private final CourseLectureService courseLectureService;

    @GetMapping("/video/{lectureId}")
    public Mono<ResponseEntity<ResourceRegion>> getLectureStream(
            @PathVariable("lectureId") Long lectureId,
            @RequestHeader HttpHeaders headers
    ) {
        return courseLectureService.getLectureStream(headers, lectureId)
                .map(lectureStreamDto -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .contentType(lectureStreamDto.getMediaType())
                        .body(lectureStreamDto.getResourceRegion())
                );
    }

}
