package hello.advanced.app.trace.threadlocal;

import hello.advanced.app.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    //쓰레드 2개가 뭔가 경합을 할떄 동시성 이슈가 발생하니까
    @Test
    void field() {
        log.info("main start");

        /*Runnable userA = new Runnable() {
            @Override
            public void run() {

            }
        }; 을 아래처럼 람다로 줄일 수 있다. */

        Runnable userA = ()->{
            fieldService.logic("userA");
        };
        Runnable userB = ()->{
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();        // Runnable userA을 넣었으니 userA 로직이 실행됨.
        sleep(2000);      // 동시성 문제가 발생 안하는 코드, FieldService에서 1초면 끝나는데, A가 완전히 끝나고나서 B로직이 수행

        //sleep(100);
        //이건 동시성 문제 발생 한다. 왜냐,  threadA가 끝나는데 1초 걸린다 근데 1초도 안돼서 threadB를 호출한다.
        // ThreadA가 nameStore 에 값을 userA로 저장했는데 ThreadA가 FieldService에서 sleep(1000)한 사이 바로 ThreadB가 들어와서 nameStore를 userB로 바꿔놨다.
        threadB.start();

        //이 상태에서 실행하면  userB에 대한 로그가 안나온다.  메인쓰레드가  threadB.start()를 시작해놓고 테스트를 끝내버린 것.

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
