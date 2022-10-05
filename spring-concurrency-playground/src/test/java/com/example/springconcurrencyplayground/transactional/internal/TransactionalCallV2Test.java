package com.example.springconcurrencyplayground.transactional.internal;

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
public class TransactionalCallV2Test {

    @Autowired
    private CallService callService;

    /**
     * 메서드를 외부 클래스로 분리하여서 문제를 해결한다!
     */
    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV2TestConfig {
        @Bean
        public CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        public InternalService internalService() {
            return new InternalService();
        }
    }

    @Slf4j
    static class CallService {

        private final InternalService internalService;

        public CallService(final InternalService internalService) {
            this.internalService = internalService;
        }

        public void external() {
            log.info("call external");
            printTsInfo();
            internalService.internal();
        }

        private void printTsInfo() {
            boolean tsActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("ts active = {}", tsActive);
        }
    }

    @Slf4j
    static class InternalService {

        @Transactional
        public void internal() {
            log.info("call internal");
            printTsInfo();
        }

        private void printTsInfo() {
            boolean tsActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("ts active = {}", tsActive);
        }
    }
}
