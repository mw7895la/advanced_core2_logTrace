package hello.advanced.app.trace.strategy.code.template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeLogTemplate {
    /**
     * 스프링에서 무슨무슨 ~template 이 다 이런방식이다. callback을 받고 중간에 실행한다.
     */

    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        callback.call();
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }
}
