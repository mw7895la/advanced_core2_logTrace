package hello.advanced.app.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;


/**
 * 필드에 전략을 보관하는 방식
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;      //생성자를 통해서 변하는 부분인 Strategy 구현체를 주입받자.

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    //큰 문맥( 큰 틀)을 여기다 적어주자
    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call();            // * 위임 *
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }
}
