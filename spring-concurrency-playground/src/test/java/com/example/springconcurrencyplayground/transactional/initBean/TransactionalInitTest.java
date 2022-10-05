package com.example.springconcurrencyplayground.transactional.initBean;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
class TransactionalInitTest {

    @Autowired
    private Test test;

    @org.junit.jupiter.api.Test
    void test() {
    }

    @TestConfiguration
    static class InitTsTestConfig {
        @Bean
        public Test test() {
            return new Test();
        }
    }

    @Slf4j
    static class Test {

        /**
         * 초기화 코드에 @Transactional을 같이 사용하면 트랜잭션이 적용되지 않는다.
         * 그 이유는, 초기화 코드가 먼저 호출되고, 그 다음에 AOP가 적용되기 때문이다.
         */
        @PostConstruct
        @Transactional
        public void initV1() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Init @PostConstructor ts Active = {}", isActive);
        }

        /**
         * 스프링 컨테이너가 전부 올라가고, @EventListener와 ApplicationReadyEvent를 통해서 컨테이너 초기화 이후 해당 메서드가 호출된다.
         * 이렇게 되면 @Transactional이 적용되는 것을 확인할 수 있다.
         */
        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Init @EventListener ts Active = {}", isActive);
        }
    }
}
