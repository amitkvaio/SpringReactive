package dev.learning;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
@Configuration
public class SecurityConfig {
    @Bean MapReactiveUserDetailsService users(@Value("${DEMO_USER:student}") String user,
            @Value("${DEMO_PASSWORD:}") String password) {
        if (password.isBlank()) throw new IllegalStateException("Set DEMO_PASSWORD before starting this teaching API");
        return new MapReactiveUserDetailsService(User.withUsername(user)
            .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password)).roles("STUDENT").build());
    }
    @Bean SecurityWebFilterChain security(ServerHttpSecurity http) {
        // Stateless curl-only teaching API: no cookies or browser session authentication.
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .securityContextRepository(org.springframework.security.web.server.context.NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(auth -> auth.pathMatchers("/actuator/health").permitAll().anyExchange().authenticated())
            .httpBasic(Customizer.withDefaults()).build();
    }
}
