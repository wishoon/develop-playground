package com.spring.playground.future;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class Async_Blocking {

    @Test
    void 논블록킹_동기() throws Exception {
        log.info("헌치가 루키에게 기능 A의 리뷰를 부탁한다 : {}", LocalDateTime.now());
        Future<String> 루키_리뷰 = 루키의_A기능_리뷰();

        log.info("헌치가 써머에게 기능 B의 리뷰를 부탁한다 : {}", LocalDateTime.now());
        Future<String> 써머_리뷰 = 써머의_B기능_리뷰();

        while(true) {
            if(루키_리뷰.isDone()) {
                log.info("루키의 리뷰 결과 : {}", 루키_리뷰.get());
                break;
            }
            log.info("헌치는 루키에게 리뷰를 다 했냐고 물어본다 : {}", LocalDateTime.now());
            Thread.sleep(1000);
        }

        while(true) {
            if(써머_리뷰.isDone()) {
                log.info("써머의 리뷰 결과 : {}", 써머_리뷰.get());
                break;
            }
            log.info("헌치는 써머에게 리뷰를 다 했냐고 물어본다 : {}", LocalDateTime.now());
            Thread.sleep(1000);
        }

        log.info("헌치는 리뷰를 반영한다 : {}", LocalDateTime.now());
        Thread.sleep(4000);
        log.info("헌치는 리뷰 반영을 완료했다 : {}", LocalDateTime.now());
    }

    public Future<String> 루키의_A기능_리뷰() throws InterruptedException {
        log.info("루키는 기능 A의 리뷰를 시작한다 : {}", LocalDateTime.now());
        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("루키는 기능 A의 리뷰를 종료한다 : {}", LocalDateTime.now());
            return "A 기능 리뷰 종료";
        });
    }

    public Future<String> 써머의_B기능_리뷰() throws InterruptedException {
        log.info("써머는 기능 B의 리뷰를 시작한다 : {}", LocalDateTime.now());
        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("써머는 기능 B의 리뷰를 종료한다 : {}", LocalDateTime.now());
            return "B 기능 리뷰 종료";
        });
    }
}
