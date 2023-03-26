package hello.advanced.app.trace.strategy;

import hello.advanced.app.trace.strategy.code.strategy.ContextV2;
import hello.advanced.app.trace.strategy.code.strategy.Strategy;
import hello.advanced.app.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.app.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {

        //이전 전략 패턴과 다른점은  하나의 context만 생성했다는 점.
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());

    }

    /**
     * 전략 패턴 익명 내부 클래스
     */
    @Test
    void strategyV2() {

        //이전 전략 패턴과 다른점은  하나의 context만 생성했다는 점.
        ContextV2 context = new ContextV2();
        context.execute(new Strategy(){     //execute 안에서 실행할 코드 조각을 넘긴다고 보면 된다.
            @Override
            public void call() {
                log.info("비즈니스 로직 1 실행");
            }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직 2 실행");
            }
        });
    }

    /**
     * 전략 패턴 람다
     */
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직 1 실행"));
        context.execute(() -> log.info("비즈니스 로직 2 실행"));
    }
}
