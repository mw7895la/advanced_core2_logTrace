package hello.advanced.app.trace.hellotrace;

import hello.advanced.app.trace.TraceId;
import hello.advanced.app.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component      //빈으로 등록하여 싱글톤으로 관리 하겠다
public class HelloTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {
        //[796bccd9] OrderController.request() //로그 시작 을 출력하는 부분
        TraceId traceId = new TraceId();
        long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);      //이게 나갔다가 나중에 end나 exception 할 때 돌아온다.
    }

    public void end(TraceStatus status) {
        //[796bccd9] OrderController.request() time=1016ms //로그 종료 를 출력하는 부분
        complete(status, null);
    }

    public void exception(TraceStatus status,Exception e) {
        //정상 처리는 위의  end메소드가 호출되지만, 예외가 발생하면  이 부분을 호출해줘야 한다.
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }
    }

    // level=0이면  아무것도 없고
    // level=1 이면 |-->  이 된다.
    // level=2     |   |--> 가 된다.

    // level =2 면서 예외가 발생하면  |   |<X-
    // level =1이면서 예외 발생하면   |<X-
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }
}
