package cn.har01d.demo.springwebfluxdemo.user;

import cn.har01d.demo.springwebfluxdemo.lock.LockManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final LockManager lockManager;
    private final UserRepository userRepository;

    public UserService(LockManager lockManager, UserRepository userRepository) {
        this.lockManager = lockManager;
        this.userRepository = userRepository;
    }

    public Mono<User> create(UserDto dto) {
        LockManager.Lock lock = lockManager.tryLock(dto.getName());
        return Mono.just(dto.getName())
                .doOnNext(name -> {
                    if (userRepository.existByName(dto.getName())) {
                        throw new IllegalArgumentException("user name exist");
                    }
                }).flatMap(name -> {
                    User user = new User();
                    user.setName(dto.getName());
                    user.setEmail(dto.getEmail());
                    return userRepository.save(user);
                })
                .doOnTerminate(lock::release);
    }

    public Mono<User> update(Integer id, UserDto dto) {
        LockManager.Lock lock = lockManager.tryLock(dto.getName());
        return userRepository.findById(id).flatMap(user -> {
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            return userRepository.save(user);
        }).doOnTerminate(lock::release);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }
}
