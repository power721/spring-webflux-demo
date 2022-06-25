package cn.har01d.demo.springwebfluxdemo.user;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public Mono<User> create(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PutMapping("/{id}")
    public Mono<User> update(@PathVariable Integer id, @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }
}
