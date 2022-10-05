package com.example.springconcurrencyplayground.transactional.rollback;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class TransactionalRollbackTest {

    @Autowired
    private RollbackService rollbackService;

    @Test
    void runTimeException() {
        assertThatThrownBy(() -> rollbackService.runtimeException())
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        assertThatThrownBy(() -> rollbackService.checkedException())
            .isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor() {
        assertThatThrownBy(() -> rollbackService.rollbackFor())
            .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig {
        @Bean
        public RollbackService rollbackService() {
            return new RollbackService();
        }
    }

    /**
     * 스프링은 왜 체크 예외는 커밋하고, 언체크 예외는 롤백할까??
     * 스프링은 기본적으로 체크 예외는 비즈니스 의미가 있을 때 사용하고, 런타임 예외는 복구 불가능한 예외로 바라본다.
     * 예를 들어, 주문에 대해서 잔고부족이라는 예외가 발생했을 때, 대기 상태로 만들어야 한다고 가정하자.
     * 언체크로 설정할 경우 전부 롤백이 되버릴 것이다!!
     * 체크로 설정할 경우 지금까지 처리들이 롤백이 되지 않고, 커밋이 될 것이다!!
     *
     * 상황에 맞는 체크와 언체크를 사용하도록 항상 유의하자!! (체크를 안하고 결과 상태를 보내서 다른 프로세스로 비즈니스 로직을 처리하는 방법도 있음!)
     */
    @Slf4j
    static class RollbackService {

        // 런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        // 체크 예외 발생: 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        // 체크 예외 rollbackFor 지정: 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception {

    }
}
