package com.kosi.kosi_safety_training_vt.dto;

import lombok.*;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureStreamDto {
    ResourceRegion resourceRegion;
    MediaType mediaType;
}