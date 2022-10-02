package com.example.springpagingplayground.presentation;

import com.example.springpagingplayground.application.StudyLogService;
import com.example.springpagingplayground.application.dto.StudyLogsPageResponse;
import com.example.springpagingplayground.application.dto.StudyLogsSliceResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/studyLogs")
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(final StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    @GetMapping("/page")
    public ResponseEntity<StudyLogsPageResponse> showPageStudyLogs(final HttpServletRequest httpRequest,
                                                                   final Pageable pageable) {
        log.info("Request URI : {}", httpRequest.getRequestURI() + "?" + httpRequest.getQueryString());
        log.info("Pageable    : {}", pageable);
        StudyLogsPageResponse studyLogsResponse = studyLogService.findPageStudyLogById(pageable);
        return ResponseEntity.ok(studyLogsResponse);
    }

    @GetMapping("/slice")
    public ResponseEntity<StudyLogsSliceResponse> showSliceStudyLogs(final HttpServletRequest httpRequest,
                                                                     final Pageable pageable) {
        log.info("Request URI : {}", httpRequest.getRequestURI() + "?" + httpRequest.getQueryString());
        log.info("Pageable    : {}", pageable);
        StudyLogsSliceResponse studyLogsResponse = studyLogService.findSliceStudyLogById(pageable);
        return ResponseEntity.ok(studyLogsResponse);
    }
}
