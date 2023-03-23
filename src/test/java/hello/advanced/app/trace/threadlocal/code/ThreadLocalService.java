package hello.advanced.app.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalService {

    /**
     *  ThreadLocal 사용.  객체니까 set()과 get()을 사용해야 한다.
     *
     *  쓰레드 로컬을 다 사용하고 나면 ThreadLocal.remove()를 해줘야 한다.  조금 이따가 알아보자.
     */
    private ThreadLocal<String> nameStore = new ThreadLocal<>();

    public String logic(String name) {
        log.info("저장 name ={} -> nameStore={}", name, nameStore.get());
        //nameStore=name;

        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}",nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
