package com.kosi.kosi_safety_training_vt.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("video")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    private Long idx;

    @Column("duration_time")
    private String durationTime;

    @Column("file_path")
    private String filePath;

    @Column("video_name")
    private String videoName;

    @Column("lecture_id")
    private Long lectureId;
}
