package hello.advanced.app.v3;

import hello.advanced.app.trace.TraceId;
import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.hellotrace.HelloTraceV2;
import hello.advanced.app.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    public void save(String itemId) {

        TraceStatus status=null;
        try {
            status = trace.begin("OrderRepositoryV1.save()");
            //저장 로직
            if (itemId.equals("ex")) {
                //ex면 예외를 터트리는 로직  // 저장을 못하고 예외가 터져서 던져진다.
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            trace.end(status);
        }catch(Exception e){
            trace.exception(status,e);
            //파라미터로 try문 안에있는 status를 넣어주려는데 status는 try문 안에서만 유효. 그래서 선언부만 밖으로 빼자.
            //여기서 예외를 먹고 예외가 밖으로 안나간다.  로그추적기 사용시 흐름을 바꾸면 안된다.
            throw e;
            //예외를 꼭 다시 던져줘야한다. 이걸 안해주면 정상흐름으로 된다.
        }

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
