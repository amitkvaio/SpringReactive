package dev.learning;
import org.springframework.messaging.rsocket.RSocketRequester;
public class DemoClient {
    public static void main(String[] args) {
        RSocketRequester requester = RSocketRequester.builder().tcp("localhost", 7000);
        try {
            String result = requester.route("greet").data("student").retrieveMono(String.class)
                .block(java.time.Duration.ofSeconds(5));
            System.out.println(result);
        } finally {
            requester.dispose();
            reactor.netty.tcp.TcpResources.disposeLoopsAndConnections();
            reactor.core.scheduler.Schedulers.shutdownNow();
        }
    }
}
