package com.example.springconcurrencyplayground.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void before() {
        Stock stock = new Stock(1L, 100L);

        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    void after() {
        stockRepository.deleteAll();
    }

    @Test
    void 재고_감소() {
        stockService.decreaseOfApplication(1L, 1L);

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(99L).isEqualTo(stock.getQuantity());
    }

    /**
     * 기대하는 결과가 나올 것인가? 나오지 않음!!
     * 그 이유는 Race Condition이 발생했기 때문
     * Race Condition은 2개 이상의 프로세스가 공유 자원을 병행적으로 읽거나 쓰는 상황을 말함. 즉, 같은 Stock에 접근했기 때문에 예상하는 결과가 나타나지 않음
     */
    @Test
    void 동시에_100개의_요청으로_재고_감소를_어플리케이션_레벨에서_처리() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseOfApplication(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(0L).isEqualTo(stock.getQuantity());
    }

    @Test
    void 동시에_100개의_요청으로_재고_감소를_인프라_레벨에서_처리() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decreaseOfDatabase(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(0L).isEqualTo(stock.getQuantity());
    }

    @Test
    void synchonized를_사용해서_동시에_100개의_요청으로_재고_감소() throws InterruptedException {
        int threadCount = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.synchronizedDecrease(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}
