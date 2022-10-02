package com.example.springpagingplayground.application;

import com.example.springpagingplayground.application.dto.StudyLogsPageResponse;
import com.example.springpagingplayground.application.dto.StudyLogsSliceResponse;
import com.example.springpagingplayground.domain.StudyLog;
import com.example.springpagingplayground.domain.StudyLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;

    public StudyLogService(final StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }

    @Transactional(readOnly = true)
    public StudyLogsPageResponse findPageStudyLogById(final Pageable pageable) {
        Page<StudyLog> studyLog = studyLogRepository.findPageAllBy(pageable);

        return StudyLogsPageResponse.of(studyLog);
    }

    @Transactional(readOnly = true)
    public StudyLogsSliceResponse findSliceStudyLogById(final Pageable pageable) {
        Slice<StudyLog> studyLog = studyLogRepository.findSliceAllBy(pageable);

        return StudyLogsSliceResponse.of(studyLog);
    }
}
