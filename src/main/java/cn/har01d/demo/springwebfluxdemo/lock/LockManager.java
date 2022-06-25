package cn.har01d.demo.springwebfluxdemo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class LockManager {
    private final ConcurrentMap<String, String> locks = new ConcurrentHashMap<>();

    public boolean tryLock(String key) {
        String name = Thread.currentThread().getName();
        String old = locks.putIfAbsent(key, name);
        boolean result = old == null || old.equals(name);
        if (result) {
            log.info("get lock {} for thread {}", key, name);
        }
        return result;
    }

    public void releaseLock(String key) {
        String name = Thread.currentThread().getName();
        boolean result = locks.remove(key, name);
        if (result) {
            log.info("release lock {} for thread {}", key, name);
        } else {
            log.warn("cannot release lock {} for thread {}", key, name);
        }
    }
}
