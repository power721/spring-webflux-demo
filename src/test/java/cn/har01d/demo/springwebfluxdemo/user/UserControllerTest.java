package cn.har01d.demo.springwebfluxdemo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    WebTestClient client;

    @BeforeEach
    void setUp(ApplicationContext context) {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void testCreateUser() throws InterruptedException {
        UserDto dto = new UserDto();
        dto.setName("test");
        dto.setEmail("email");
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; ++i) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(10));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.post()
                        .uri("/users")
                        .bodyValue(dto)
                        .exchange()
                        .expectBody();
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        client.get().uri("/users").exchange()
                .expectBody().jsonPath("$[0].id").isEqualTo(1);
    }
}
