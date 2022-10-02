package com.example.springpagingplayground.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "studyLog_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudyLog studyLog;

    private String content;

    public Comment(final Long id, final StudyLog studyLog, final String content) {
        this.id = id;
        this.studyLog = studyLog;
        this.content = content;
    }
}
