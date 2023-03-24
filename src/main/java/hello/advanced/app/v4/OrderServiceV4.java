package hello.advanced.app.v4;

import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.logtrace.LogTrace;
import hello.advanced.app.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {
        /**
         * Controller에선 반환 타입이 String이었다. 여긴 void다
         * 지네릭스는 임의의 참조형 타입이기 때문에  primitive type인  void가 아닌 Void를 적어줬다.
         */

        // AbstractTemplate 를 상속받은 자식클래스가 정의되고 얘를 객체로 생성한다.
        AbstractTemplate<Void> template = new AbstractTemplate<Void>(trace) {
            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };
        //@RestController니까 JSON 형식으로 ok를 리턴
        template.execute("OrderService.orderItem()");

    }
}
