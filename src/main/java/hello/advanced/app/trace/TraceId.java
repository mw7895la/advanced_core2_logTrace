package hello.advanced.app.trace;

import java.util.UUID;

public class TraceId {
    private String id;  // [796bccd9] 이 부분을 위한 것.
    private int level;  // |--> 이 부분을 위한 것.

    public TraceId() {
        this.id= createId();
        this.level=0;
    }

    private TraceId(String id, int level) {     //내부에서만 쓰는 private
        this.id = id;
        this.level = level;
    }

    private String createId() {
        return UUID.randomUUID().toString().substring(0,8);   //너무 길게 만들면 안되니까  7자로 하겠다.
    }

    public TraceId createNextId(){
        //다음 id를 편하게 만들자.
        return new TraceId(id, level + 1);
        //[796bccd9] OrderController.request()
        //[796bccd9] |-->OrderService.orderItem()       <- 이부분을 표현하기 위한것. 트랜잭션 ID는 똑같고 레벨을 표현하기 위해서 +1을 한 것.
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel(){
        //첫 번째 레벨인가 알아보려고.
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
