package com.example.springpagingplayground.application.dto;

import com.example.springpagingplayground.domain.StudyLog;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyLogsPageResponse {

    private List<StudyLogResponse> data;
    private Long totalCount;

    private StudyLogsPageResponse(final List<StudyLogResponse> data, final Long totalCount) {
        this.data = data;
        this.totalCount = totalCount;
    }

    public static StudyLogsPageResponse of(final Page<StudyLog> studyLogs) {
        List<StudyLogResponse> responses = studyLogs.stream()
            .map(StudyLogResponse::of)
            .collect(Collectors.toList());

        return new StudyLogsPageResponse(responses, studyLogs.getTotalElements());
    }
}
