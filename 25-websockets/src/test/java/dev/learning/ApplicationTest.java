package dev.learning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {"DEMO_PASSWORD=test-only", "DEMO_USER=student"})
class ApplicationTest {
    static { reactor.test.StepVerifier.setDefaultTimeout(java.time.Duration.ofSeconds(5)); }
    @Autowired Environment environment;
    WebTestClient client;
    @BeforeEach void client() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + environment.getProperty("local.server.port")).build();
    }

    @Test void exchangesMessageOverRealSocket() {
        var client = new org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient();
        var received = new java.util.concurrent.atomic.AtomicReference<String>();
        var exchange = client.execute(java.net.URI.create("ws://localhost:" + environment.getProperty("local.server.port") + "/chat"), session ->
            session.send(reactor.core.publisher.Mono.just(session.textMessage("hello")))
                .then(session.receive().next().doOnNext(m -> received.set(m.getPayloadAsText())).then()));
        reactor.test.StepVerifier.create(exchange).expectComplete().verify(java.time.Duration.ofSeconds(5));
        assertEquals("echo: hello", received.get());
    }
}
