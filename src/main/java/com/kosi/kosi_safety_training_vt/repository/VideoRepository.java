package com.kosi.kosi_safety_training_vt.repository;

import com.kosi.kosi_safety_training_vt.entity.Video;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VideoRepository extends R2dbcRepository<Video, Long> {
    // lectureId 컬럼 기반 조회
    Flux<Video> findByLectureId(Long lectureId);
}
