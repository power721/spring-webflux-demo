package cn.har01d.demo.springwebfluxdemo.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class LockManager {
    private final ConcurrentMap<String, String> locks = new ConcurrentHashMap<>();

    public Lock tryLock(String key) {
        String value = UUID.randomUUID().toString();
        String old = locks.putIfAbsent(key, value);
        if (old == null) {
            log.info("get lock {} for {}", key, value);
            return new Lock(key, value);
        } else {
            throw new IllegalStateException("Cannot get the lock.");
        }
    }

    public class Lock {
        private final String key;
        private final String value;

        private Lock(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public void release() {
            boolean result = locks.remove(key, value);
            if (result) {
                log.info("release lock {} for {}", key, value);
            }
        }
    }
}
