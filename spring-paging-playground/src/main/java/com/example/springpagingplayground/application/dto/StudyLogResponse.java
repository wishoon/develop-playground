package com.example.springpagingplayground.application.dto;

import com.example.springpagingplayground.domain.StudyLog;
import lombok.ToString;

@ToString
public class StudyLogResponse {

    private Long id;
    private String content;

    private StudyLogResponse(final Long id, final String content) {
        this.id = id;
        this.content = content;
    }

    public static StudyLogResponse of(final StudyLog studyLog) {
        return new StudyLogResponse(studyLog.getId(), studyLog.getContent());
    }
}
