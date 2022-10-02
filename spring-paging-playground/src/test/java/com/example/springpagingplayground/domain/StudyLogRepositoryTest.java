package com.example.springpagingplayground.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@Slf4j
@DataJpaTest
class StudyLogRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudyLogRepository studyLogRepository;

    @Nested
    class OneToManyOfSelect {

        @Test
        void 일대다_관계에서의_조회는_일쪽의_데이터가_반복해서_조회되는_문제가_있다() {
            // given
            for (int i = 1; i <= 10; i++) {
                StudyLog studyLog = studyLogRepository.save(new StudyLog(null, "내용" + i));
                for (int j = 1; j <= 10; j++) {
                    studyLog.addComment(new Comment(null, studyLog, "댓글 내용"));
                    studyLogRepository.flush();
                }
            }

            entityManager.clear();

            // when
            List<StudyLog> extract = studyLogRepository.findCommentsAllByNotDistinct();

            // then
            for (StudyLog data : extract) {
                log.info("Studylog Id : {}, Comment Size : {}", data.getId(), data.getComments().size());
            }
        }

        /**
         * 컬렉션 페치 조인으로 사용해서 SQL을 여러번 조회하는 문제를 해결할 수 있다.
         * 또한, distinct 키워드를 통해서 어플리케이션 단에서 중복을 제거할 수 있다.
         * 다만, 이렇게 될 경우 페이징이 불가능하다.
         */
        @Test
        void 일대다_관계에서의_조회_문제를_해결하기_위해_distinct를_사용하여_중복_조회를_해결한다() {
            // given
            for (int i = 1; i <= 10; i++) {
                StudyLog studyLog = studyLogRepository.save(new StudyLog(null, "내용" + i));
                for (int j = 1; j <= 10; j++) {
                    studyLog.addComment(new Comment(null, studyLog, "댓글 내용"));
                    studyLogRepository.flush();
                }
            }

            entityManager.clear();

            // when
            List<StudyLog> extract = studyLogRepository.findCommentsAllByOnDistinct();

            // then
            for (StudyLog data : extract) {
                log.info("Studylog Id : {}, Comment Size : {}", data.getId(), data.getComments().size());
            }
        }

        /**
         * WARN 16980 --- [    Test worker] o.h.h.internal.ast.QueryTranslatorImpl   : HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
         * OOM이 발생할 가능성이 99.999%!
         * Spring Data JPA 에서는 List 대신 Page 객체를 사용하면 로딩 시점에 예외 발생! - Page<StudyLog> findPage...(Pageable pageable);
         */
        @Test
        void 일대다_관계에서_페치조인을_하고_페이징을_하면_메모리_상에서_페이징이_된다() {
            // given
            for (int i = 1; i <= 10; i++) {
                StudyLog studyLog = studyLogRepository.save(new StudyLog(null, "내용" + i));
                for (int j = 1; j <= 10; j++) {
                    studyLog.addComment(new Comment(null, studyLog, "댓글 내용"));
                    studyLogRepository.flush();
                }
            }

            entityManager.clear();

            // when
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<StudyLog> extract = studyLogRepository.findPageAndCommentsAll(pageRequest);

            // then
            for (StudyLog data : extract) {
                log.info("Studylog Id : {}, Comment Size : {}", data.getId(), data.getComments().size());
            }
        }

        /**
         * BatchSize를 사용하면, IN 절을 사용해 한번에 가져온다.
         * 항상 페이징에 유념해야 할점은, FROM 절을 기준으로 페이징을 한다는 점을 명심하자
         */
        @Test
        void 일대다_관계에서_페치조인_문제를_해결하기_위해_BatchSize를_설정하여_해결한다() {
            // given
            for (int i = 1; i <= 10; i++) {
                StudyLog studyLog = studyLogRepository.save(new StudyLog(null, "내용" + i));
                for (int j = 1; j <= 10; j++) {
                    studyLog.addComment(new Comment(null, studyLog, "댓글 내용"));
                    studyLogRepository.flush();
                }
            }

            entityManager.clear();

            // when
            PageRequest pageRequest = PageRequest.of(0, 5);
            List<StudyLog> extract = studyLogRepository.findPageOfBatchSizeAndCommentsAll(pageRequest);

            // then
            for (StudyLog data : extract) {
                log.info("Studylog Id : {}, Comment Size : {}", data.getId(), data.getComments().size());
            }
        }
    }

    @Nested
    class UseDataJpaPaging {

        @Test
        void Page_객체를_통해서_페이지네이션을_처리할_수_있다() {
            // given
            for (int i = 1; i <= 20; i++) {
                studyLogRepository.save(new StudyLog(null, "내용" + i));
            }
            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Page<StudyLog> extract = studyLogRepository.findPageAllBy(pageRequest);

            // then
            assertThat(extract.getTotalPages()).isEqualTo(2);
        }

        @Test
        void Slice_객체를_통해서_페이지네이션을_처리할_수_있다() {
            // given
            for (int i = 1; i <= 20; i++) {
                studyLogRepository.save(new StudyLog(null, "내용" + i));
            }
            PageRequest pageRequest = PageRequest.of(0, 10);

            // when
            Slice<StudyLog> extract = studyLogRepository.findSliceAllBy(pageRequest);

            // then
            assertThat(extract.hasNext()).isTrue();
        }
    }
}
