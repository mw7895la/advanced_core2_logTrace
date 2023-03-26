package hello.advanced.app.v5;

import hello.advanced.app.trace.callback.TraceCallback;
import hello.advanced.app.trace.callback.TraceTemplate;
import hello.advanced.app.trace.logtrace.LogTrace;
import hello.advanced.app.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController     //@Controller + @ResponseBody
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    /**
     *  TraceTemplate에서 LogTrace가 필요하다 그래서 trace를 주입해준다.  TraceTemplate 은 LogTrace가 필요하다.
     *  아래서 딱 한번 만들어 놓는다. 그러면 싱글톤이라 하나만 존재한다.
     */
    @Autowired
    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    /**
     * 콜백 패턴
     */

    @GetMapping("/v5/request")
    public String request(String itemId) {      //템플릿을 실행하면서  callback을 전달한다.
        return template.execute("OrderController.request()", new TraceCallback<>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        });


    }


}
