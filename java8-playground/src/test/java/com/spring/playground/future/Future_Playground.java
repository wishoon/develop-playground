package com.spring.playground.future;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class Future_Playground {

    @Test
    void future_interface_실험하기() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> callableTask = () -> {
            log.info("Callable Task Start : {}", LocalDateTime.now());
            Thread.sleep(1000);
            return "Task Result";
        };

        Future<String> future = executor.submit(callableTask);

        log.info("Waiting the task done : {}", LocalDateTime.now());
        log.info("Task is Done : {}", future.isDone());
        String result = future.get();
        log.info("Task is Done : {}", future.isDone());
        log.info("Finish the task done : {}", LocalDateTime.now());
    }

    @Test
    void Future_Interface_실험하기() throws InterruptedException {
        int time = 0;
        log.info("헌치가 루키에게 기능 A의 리뷰를 부탁한다 : {}", LocalDateTime.now());
        루키의_A기능_리뷰();

        log.info("헌치가 써머에게 기능 B의 리뷰를 부탁한다 : {}", LocalDateTime.now());

        while (time < 4) {
            Thread.sleep(1000);
            log.info("헌치는 할 일을 한다 : {}", LocalDateTime.now());
            time++;
        }
        log.info("헌치는 할 일을 마쳤다 : {}", LocalDateTime.now());
    }

    public void 루키의_A기능_리뷰() {
        log.info("루키는 기능 A의 리뷰를 시작한다 : {}", LocalDateTime.now());

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("루키는 리뷰를 끝냈다고 알림을 보낸다 : {}", LocalDateTime.now());
        });
    }
}
