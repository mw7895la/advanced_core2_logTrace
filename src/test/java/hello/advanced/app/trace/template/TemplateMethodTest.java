package hello.advanced.app.trace.template;

import hello.advanced.app.trace.template.code.AbstractTemplate;
import hello.advanced.app.trace.template.code.SubClassLogic1;
import hello.advanced.app.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    /**
     * 템플릿 메서드 패턴을 왜 사용해야 되는지 알아보자
     * 뭔가 로그를 찍는 것과 비즈니스 로직을 섞는 상황이다.   (핵심 + 부가기능)
     *
     * 비즈니스 로직은 변하는 부분이다   log.info("비즈니스 로직 1 실행");  -> log.info("비즈니스 로직 2 실행");
     */

    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;

        log.info("resultTime={}", resultTime);
    }

    /**
     * 템플릿 메서드 패턴을 적용한다.
     *
     * 아래 템플릿 메서드 패턴을 적용하면,  이전에 logic1 , logic2 부분에서 만약  log.info("resultTime={}", resultTime); 부분의 resultTime을 변경해야 된다면
     * 2번 변경해야되지만,  아래 처럼 추상 클래스 사용시 execute() 부분에서 한 번만 변경하면 된다.   ** 이게 중요하다
     * 한 번만 변경하냐? 두 번 변경해야되냐?  " 단일 책임 원칙 " 이 잘 갖춰진 거라 볼 수 있다.   - 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것 -
     */
    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();  //자동 형변환  (상속관계)

        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();  //자동 형변환  (상속관계)

        template2.execute();
    }

    /** 익명 객체
     *  {  } 의 내용으로 내것을 물려주는 자식 클래스를 만든다. 오버라이딩 할 건 하자
     */
    @Test
    void templateMethodV2() {
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 1 실행");
            }
        };
        log.info("클래스 이름1 ={}", template1.getClass());
        //클래스 이름1 =class hello.advanced.app.trace.template.TemplateMethodTest$1
        // 익명클래스가 정의된 위치가 어디냐면 TemplateMethodTest 안에 있기 때문에 내부 클래스다. 그리고 이름이 없기 때문에 자바가 임의로 TemplateMethodTest$1로 만들어 준것.
        //강의자료 부분 TemplateMethodTest$1이 맞다

        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 2 실행");
            }
        };
        log.info("클래스 이름2 ={}", template2.getClass());
        template2.execute();
    }
}
