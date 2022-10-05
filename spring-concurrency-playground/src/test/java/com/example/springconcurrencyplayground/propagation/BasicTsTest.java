package com.example.springconcurrencyplayground.propagation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Slf4j
@SpringBootTest
class BasicTsTest {

    @Autowired
    private PlatformTransactionManager tsManager;

    @TestConfiguration
    static class Config {
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus status = tsManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 커밋 시작");
        tsManager.commit(status);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback() {
        log.info("트랜잭션 시작");
        TransactionStatus status = tsManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 롤백 시작");
        tsManager.rollback(status);
        log.info("트랜잭션 롤백 완료");
    }

    /**
     * 로그를 확인해보면, 같은 커넥션을 사용하고 있는 것을 확인할 수 있다.
     * 이것은, 첫 트랜잭션이 커넥션 사용 후, 반납하고 다음 트랜잭션이 해당 커넥션을 사용했기 때문이다. 즉, 둘은 완전히 다른 커넥션이다.
     * 이를 구분할 수 있는 방법은, 히카리 커넥션 프록시 객체의 주소를 통해서 확인할 수 있다.
     */
    @Test
    void double_commit() {
        log.info("트랜잭션 1 시작");
        TransactionStatus ts1 = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 1 커밋");
        tsManager.commit(ts1);

        log.info("트랜잭션 2 시작");
        TransactionStatus ts2 = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 2 커밋");
        tsManager.commit(ts2);
    }

    @Test
    void double_commit_and_rollback() {
        log.info("트랜잭션 1 시작");
        TransactionStatus ts1 = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 1 커밋");
        tsManager.commit(ts1);

        log.info("트랜잭션 2 롤백 시작");
        TransactionStatus ts2 = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션 2 롤백 완료");
        tsManager.rollback(ts2);
    }

    /**
     * 외부, 내부에서 커밋을 2번하는데도, 정상적으로 수행되는 이유는 내부 트랜잭션을 시작할 때, Participating in existing transaction이라는 메시지를 확인할 수 있다.
     * 외부 트랜잭션을 시작하거나 커밋할 때는 물리 트랜잭션을 시작하고, DB 커넥션을 통해 커밋하는 로그를 확인할 수 있다. 하지만 내부 트랜잭션에서는 커밋 로그를 확인할 수 없다.
     * 즉, 외부와 내부를 사용하는 경우 스프링이 처음 시작한 트랜잭션을 관리하도록 해준다.
     */
    @Test
    void inner_commit() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
        log.info("내부 트랜잭션 커밋");
        tsManager.commit(inner);

        log.info("외부 트랜잭션 커밋");
        tsManager.commit(outer);
    }

    @Test
    void outer_rollback() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = tsManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 커밋");
        tsManager.commit(inner);

        log.info("외부 트랜잭션 커밋");
        tsManager.rollback(outer);
    }

    /**
     * 내부 트랜잭션이 롤백할 경우, 실제 물리 트랜잭션은 롤백하지 않는다. 대신에 트랜잭션 동기화 메니저에 롤백 전용으로 표시한다.
     * 이를 통해서 외부 트랜잭션은 커밋이 되었지만, 롤백 마크가 표시 되어 있어 롤백이 된다.
     *
     * 해당 경우는 문제를 분명히 해야 하기 때문에, UnexpectedRollbackException 런타임 예외를 던진다.
     */
    @Test
    void inner_rollback() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = tsManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = tsManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백");
        tsManager.rollback(inner);

        log.info("외부 트랜잭션 커밋");
        assertThatThrownBy(() -> tsManager.commit(outer))
            .isInstanceOf(UnexpectedRollbackException.class);
    }
}
