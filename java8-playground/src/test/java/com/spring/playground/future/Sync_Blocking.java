package com.spring.playground.future;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class Sync_Blocking {

    @Test
    void 블록킹_등기() throws Exception {
        log.info("헌치가 루키에게 기능 A의 리뷰를 부탁한다 : {}", LocalDateTime.now());
        log.info("리뷰 결과 : {}" + 루키의_A기능_리뷰());

        log.info("헌치는 리뷰를 반영한다 : {}", LocalDateTime.now());
        log.info("헌치는 리뷰 반영을 완료했다 : {}", LocalDateTime.now());
    }

    public String 루키의_A기능_리뷰() throws InterruptedException {
        log.info("루키는 기능 A의 리뷰를 시작한다 : {}", LocalDateTime.now());
        Thread.sleep(3000);
        log.info("루키는 기능 A의 리뷰를 종료한다 : {}", LocalDateTime.now());

        return "A 기능 리뷰 종료";
    }
}
