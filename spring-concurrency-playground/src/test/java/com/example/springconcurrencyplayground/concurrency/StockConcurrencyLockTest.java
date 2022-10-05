package com.example.springconcurrencyplayground.concurrency;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StockConcurrencyLockTest {

    /**
     * 1. Pessimistic Lock
     *  - 실제로 데이터에 Lock을 걸어서 정합성을 맞추는 방법. (row, table 에 lock)
     * 2. Optimistic Lock
     *  - 실제로 Lock을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법
     *    데이터를 읽은 후에 update를 수행할 때, 현재 내가 읽은 버전이 맞는지 확인하며 업데이트를 수행
     *    만약 내가 읽은 버전에서 수정사항이 생기면 쿼리가 실패함. 해당 경우, application에서 읽은 후, 다시 작업을 수행
     * 3. Named Lock
     *  - 이름을 가진 metadata locking 입니다. 이름을 가진 lock을 획득한 후 해제할때까지 다른 세션은 이 lock을 획득할 수 없도록 합니다.
     *    주의할점으로는 transaction이 종료될 때 lock이 자동으로 해제되지 않습니다. 별도의 명령어로 해제를 수행해주거나 선점시간이 끝나게 됩니다.
     */
}
