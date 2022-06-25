package cn.har01d.demo.springwebfluxdemo.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
public class UserRepository {
    private final AtomicInteger id = new AtomicInteger();
    private final ConcurrentMap<Integer, User> store = new ConcurrentHashMap<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Mono<User> save(User user) {
        return Mono.fromCallable(() -> {
            if (user.getId() == null) {
                user.setId(id.incrementAndGet());
            }

            store.put(user.getId(), user);
            log.info("users: {}", store.size());
            return user;
        }).publishOn(Schedulers.fromExecutor(threadPool));
    }

    public Mono<User> findById(Integer id) {
        if (store.containsKey(id)) {
            return Mono.just(store.get(id));
        }
        return Mono.empty();
    }

    public boolean existByName(String name) {
        return store.values().stream().anyMatch(user -> user.getName().equals(name));
    }

    public Flux<User> findAll() {
        return Flux.fromIterable(store.values());
    }
}
