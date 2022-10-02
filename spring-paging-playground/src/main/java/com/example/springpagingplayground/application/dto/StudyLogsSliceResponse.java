package com.example.springpagingplayground.application.dto;

import com.example.springpagingplayground.domain.StudyLog;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Slice;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudyLogsSliceResponse {

    private List<StudyLogResponse> data;
    private boolean isNext;

    public StudyLogsSliceResponse(final List<StudyLogResponse> data, final boolean isNext) {
        this.data = data;
        this.isNext = isNext;
    }

    public static StudyLogsSliceResponse of(final Slice<StudyLog> studyLog) {
        List<StudyLogResponse> responses = studyLog.stream()
            .map(StudyLogResponse::of)
            .collect(Collectors.toList());

        return new StudyLogsSliceResponse(responses, studyLog.hasNext());
    }
}
