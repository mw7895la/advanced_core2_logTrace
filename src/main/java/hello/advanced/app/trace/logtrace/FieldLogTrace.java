package hello.advanced.app.trace.logtrace;

import hello.advanced.app.trace.TraceId;
import hello.advanced.app.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace implements LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    // TraceId를 어딘가에는 들고 있어야한다. 여기선 필드에 가지고 있는다.
    private TraceId traceIdHolder;
    //traceId 동기화, 동시성 이슈 발생  // 기존에는 파라미터를 넘겼다면 이제는 보관해놓고 쓰는 것.


    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceIdHolder;        //syncTraceId()를 호출하고 나면 traceIdHolder에 값이 들어가 있는게 보장이 된다.
        long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);      //이 데이터가 나갔다가 나중에 end나 exception 할 때 HelloTraceV2로 돌아온다.
    }

    /**
     * Level 이  0 - 1 - 2 으로 올라가는 경우
     */
    private void syncTraceId(){
        if (traceIdHolder == null) {        //null이면 처음 최초로 호출한 경우다. 그럼 traceId를 만든다.
            traceIdHolder = new TraceId();
        }else{
            //처음이 아니라면 레벨 깊이를 +1 해준다.
            traceIdHolder = traceIdHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
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

        releaseTraceId();
    }

    /**
     * Level 이  2 - 1 - 0 으로 내려가는 경우
     */
    private void releaseTraceId() {
        if (traceIdHolder.isFirstLevel()) {
            // C -> S -> R -> S -> C 마지막에 다 나왔을 때 로그가 끝난다는 것이니까
            traceIdHolder = null;       //destroy
        }else{
            //첫번쨰 Level이 아니라 중간단계에 있을 떄는
            traceIdHolder = traceIdHolder.createPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }
}
