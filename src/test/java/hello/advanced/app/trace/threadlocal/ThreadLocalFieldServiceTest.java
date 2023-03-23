package hello.advanced.app.trace.threadlocal;

import hello.advanced.app.trace.threadlocal.code.FieldService;
import hello.advanced.app.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalFieldServiceTest {

    private ThreadLocalService service = new ThreadLocalService();


    @Test
    void field() {
        log.info("main start");

        /*Runnable userA = new Runnable() {
            @Override
            public void run() {

            }
        }; 을 아래처럼 람다로 줄일 수 있다. */

        Runnable userA = ()->{
            service.logic("userA");
        };
        Runnable userB = ()->{
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();

        //sleep(2000);        // 동시성 발생 안하는 2000millis , FieldServiceTest와 다르게(ThreadB 저장시 nameStore에는 userA가 저장되어 있었음) ThreadB가 저장할떄도 nameStore가 null이다.
        sleep(100);
        threadB.start();
        /**
         *  각 쓰레드마다 본인의 저장소에 별도로 저장하기 때문에  각각 원하는 것을 얻을 수 있다.
         */


        sleep(3000);        //메인 쓰레드 종료 대기
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
