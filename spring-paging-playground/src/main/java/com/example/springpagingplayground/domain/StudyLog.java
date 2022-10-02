package com.example.springpagingplayground.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class StudyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "studyLog", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public StudyLog(final Long id, final String content) {
        this.id = id;
        this.content = content;
    }

    public void addComment(final Comment comment) {
        comments.add(comment);
    }
}
