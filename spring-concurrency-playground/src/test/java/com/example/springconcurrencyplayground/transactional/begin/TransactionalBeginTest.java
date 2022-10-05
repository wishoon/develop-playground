package com.example.springconcurrencyplayground.transactional.begin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TransactionalBeginTest {

    @Autowired
    private BasicService basicService;

    @Test
    void tsTest() {
        basicService.ts();
        basicService.nonTs();
    }

    @TestConfiguration
    static class TsApplyBasicConfig {
        @Bean
        public BasicService basicService() {
            return new BasicService();
        }
    }

    /**
     * @Transactionl 애노테이션이 특정 클래스나 메서드에 하나라도 있으면, 트랜잭션 AOP는 프록시를 만들어서 스프링 컨테이너에 등록한다. (클래스를 대상으로 프록시를 만든다.)
     * 그리고 실제 BasicService 대신에 프록시를 스프링 빈에 등록한다. 즉, tsTest에서 호출한 BasicService는 프록시를 호출한 것이다.
     */
    @Slf4j
    static class BasicService {

        /**
         * 클라이언트가 ts를 호출하면, 프록시의 ts가 호출된다. 여기서 프록시는 ts가 트랜잭션 대상인지 확인하고, 적용대상일 경우 트랜잭션을 수행한다.
         * ts의 로직이 끝나고 프록시로 돌아오면, 트랜잭션 로직을 커밋 or 롤백을 진행한다.
         */
        @Transactional
        public void ts() {
            log.info("call ts");
            boolean tsActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("ts active={}", tsActive);
        }

        public void nonTs() {
            log.info("call nonTs");
            boolean tsActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("ts active={}", tsActive);
        }
    }
}
