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
class TransactionalOrderTest {

    @Autowired
    private LevelService levelService;

    @Test
    void orderTest() {
        levelService.write();
        levelService.read();
    }

    @TestConfiguration
    static class ApplyLevelConfig {
        @Bean
        public LevelService levelService() {
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {


        @Transactional(readOnly = false)
        public void write() {
            log.info("call write");
            printTsInfo();
        }

        public void read() {
            log.info("call read");
            printTsInfo();
        }

        private void printTsInfo() {
            boolean tsActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("ts active = {}", tsActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("ts readOnly = {}", readOnly);
        }
    }
}
