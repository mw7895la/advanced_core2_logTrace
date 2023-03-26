package hello.advanced.app.trace.callback;

import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.logtrace.LogTrace;
import org.springframework.beans.factory.annotation.Autowired;

public class TraceTemplate {
    private final LogTrace trace;

    @Autowired
    public TraceTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;
        try {
            status=trace.begin(message);        //status = trace.begin("OrderController.request()");

            //로직 호출
            T result = callback.call();        // Controller를 예로 들면  orderService.orderItem(itemId); 이 부분이다.
            //로직 종료
            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status,e);
            throw e;
        }
    }
}
