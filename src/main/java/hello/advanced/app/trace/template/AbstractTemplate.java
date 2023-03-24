package hello.advanced.app.trace.template;

import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.logtrace.LogTrace;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTemplate<T> {
    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }


    //반환 타입이 조금씩 다 다르기 때문에.. 어디는 String을 반환하고 어디는 void... 그래서 제네릭을 사용한다.
    public T execute(String message) {
        TraceStatus status = null;
        try {
            status=trace.begin(message);        //status = trace.begin("OrderController.request()");

            //로직 호출
            T result = call();          // Controller를 예로 들면  orderService.orderItem(itemId); 이 부분이다.
            //로직 종료
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status,e);
            throw e;
        }
    }

    protected abstract T call();

}
