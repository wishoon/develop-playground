package com.example.springpagingplayground.domain;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {

    @Query("select s from StudyLog s join fetch s.comments c")
    List<StudyLog> findCommentsAllByNotDistinct();

    @Query("select distinct s from StudyLog s join fetch s.comments c")
    List<StudyLog> findCommentsAllByOnDistinct();

    @Query("select distinct s from StudyLog s join fetch s.comments c")
    List<StudyLog> findPageAndCommentsAll(Pageable pageable);

    @Query("select distinct s from StudyLog s join s.comments c")
    List<StudyLog> findPageOfBatchSizeAndCommentsAll(Pageable pageable);

    Page<StudyLog> findPageAllBy(Pageable pageable);

    Slice<StudyLog> findSliceAllBy(Pageable pageable);
}
