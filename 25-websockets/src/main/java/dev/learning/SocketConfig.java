package dev.learning;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
@Configuration
public class SocketConfig {
    @Bean WebSocketHandler echoHandler() {
        return session -> session.send(session.receive().map(message -> session.textMessage("echo: " + message.getPayloadAsText())));
    }
    @Bean HandlerMapping sockets(WebSocketHandler echoHandler) { return new SimpleUrlHandlerMapping(Map.of("/chat", echoHandler), -1); }
    @Bean WebSocketHandlerAdapter adapter() { return new WebSocketHandlerAdapter(); }
}
