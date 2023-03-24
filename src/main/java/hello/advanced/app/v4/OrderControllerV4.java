package hello.advanced.app.v4;

import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.logtrace.LogTrace;
import hello.advanced.app.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController     //@Controller + @ResponseBody
@RequiredArgsConstructor
public class OrderControllerV4 {

    private final OrderServiceV4 orderService;
    private final LogTrace trace;       //주입받자

    /**
     * 추상 템플릿을 사용하자 !
     */

    @GetMapping("/v4/request")
    public String request(String itemId) {

        // AbstractTemplate 를 상속받은 자식클래스가 정의되고 얘를 객체로 생성한다.
        AbstractTemplate<String> template = new AbstractTemplate<String>(trace) {
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };
        //@RestController니까 JSON 형식으로 ok를 리턴
        String result = template.execute("OrderController.request()");
        log.info("result {}",result);
        return result;

 /*       TraceStatus status=null;
        try {
            status = trace.begin("OrderController.request()");
            //orderService.orderItem(itemId);
            trace.end(status);
            //return "ok";
        }catch(Exception e){
            trace.exception(status,e);
            throw e;
        }*/


    }


}
