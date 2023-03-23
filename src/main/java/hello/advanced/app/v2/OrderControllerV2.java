package hello.advanced.app.v2;

import hello.advanced.app.trace.TraceStatus;
import hello.advanced.app.trace.hellotrace.HelloTraceV1;
import hello.advanced.app.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     //@Controller + @ResponseBody
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;       //주입받자

    @GetMapping("/v2/request")
    public String request(String itemId) {

        TraceStatus status=null;
        //http://localhost:8080/v1/request?itemId=hello
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(),itemId);
            trace.end(status);
            return "ok";
        }catch(Exception e){
            trace.exception(status,e);
            //파라미터로 try문 안에있는 status를 넣어주려는데 status는 try문 안에서만 유효. 그래서 선언부만 밖으로 빼자.
            //여기서 예외를 먹고 예외가 밖으로 안나간다.  로그추적기 사용시 흐름을 바꾸면 안된다.
            throw e;
            //예외를 꼭 다시 던져줘야한다. 이걸 안해주면 정상흐름으로 된다.
        }
        //그런데, 예외가 터졌을 때는 begin의 request()는 나오는데 Service부분에서 예외가 터져서 end가 호출이 안되고 나가버린다. 그래서 try~catch 해줘야해.

    }


}
