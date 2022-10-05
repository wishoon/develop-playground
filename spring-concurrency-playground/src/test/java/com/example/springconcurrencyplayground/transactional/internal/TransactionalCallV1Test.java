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
class TransactionalCallV1Test {

    @Autowired
    private CallService callService;

    @Test
    void internalCall() {
        callService.internal();
    }

    /**
     * @Transactional이 적용되지 않는 것을 확인할 수 있다!!
     * 클라이언트는 먼저 external을 호출한다. 여기에는 @Transactional이 없기 때문에 적용이 안된 상태로 실제 객체를 호출한다.
     * 여기서 문제가 발생하는데, external이 끝난 후 internal을 호출할 때 현재 객체, 즉 프록시가 아닌 실제 객체를 호출하게 된다!!!
     * 즉, 프록시를 호출하는 것이 아니기 때문에 @Transactional이 적용되지 않는다...!
     */
    @Test
    void externalCall() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig {
        @Bean
        public CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {

        public void external() {
            log.info("call external");
            printTsInfo();
            internal();
        }

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
