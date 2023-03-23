package hello.advanced.app.v3;

import hello.advanced.app.trace.TraceId;
import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.hellotrace.HelloTraceV2;
import hello.advanced.app.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        TraceStatus status=null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        }catch(Exception e){
            trace.exception(status,e);
            //파라미터로 try문 안에있는 status를 넣어주려는데 status는 try문 안에서만 유효. 그래서 선언부만 밖으로 빼자.
            //여기서 예외를 먹고 예외가 밖으로 안나간다.  로그추적기 사용시 흐름을 바꾸면 안된다.
            throw e;
            //예외를 꼭 다시 던져줘야한다. 이걸 안해주면 정상흐름으로 된다.
        }

    }
}
