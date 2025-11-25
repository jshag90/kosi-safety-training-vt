package com.kosi.kosi_safety_training_vt.service;

import com.kosi.kosi_safety_training_vt.dto.LectureStreamDto;
import com.kosi.kosi_safety_training_vt.entity.Video;
import com.kosi.kosi_safety_training_vt.repository.VideoRepository;
import com.kosi.kosi_safety_training_vt.util.VideoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Service
@RequiredArgsConstructor
public class CourseLectureService {

    private final VideoRepository videoRepository;

    public Mono<LectureStreamDto> getLectureStream(HttpHeaders httpHeaders, Long lectureId) {
        return videoRepository.findByLectureId(lectureId)
                .next() // 첫 번째 영상 선택
                .switchIfEmpty(Mono.error(new RuntimeException("해당 강의의 비디오가 존재하지 않습니다.")))
                .flatMap(findVideo -> {
                    // Virtual Thread executor 활용, blocking 파일 읽기 안전하게 수행
                    Callable<LectureStreamDto> task = () -> {
                        File videoFile = new File(findVideo.getFilePath());
                        Resource video = new FileSystemResource(videoFile);

                        return LectureStreamDto.builder()
                                .mediaType(VideoUtil.getMediaType(findVideo.getVideoName()))
                                .resourceRegion(VideoUtil.getResourceRegion(httpHeaders, video.contentLength(), video))
                                .build();
                    };
                    return Mono.fromCallable(task);
                });
    }

}
