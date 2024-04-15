//package BookShopRest.BookShopRest.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.SecurityBuilder;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//@Configuration
//@EnableWebFluxSecurity // Update for reactive security
//public class WebSecurityConfig {
//
//    private final ReactiveUserDetailsService userDetailsService;
//
//    public WebSecurityConfig(ReactiveUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // Use BCrypt for secure password hashing
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf().disable() // Disable CSRF protection due to REST API nature (consider alternatives for stateful UIs)
//                .authorizeExchange()
//                .pathMatchers("/authenticate").permitAll() // Allow unauthenticated access to authentication endpoint
//                .anyExchange().authenticated() // All other requests require authentication
//                .and()
//                .securityContextRepository(securityContextRepository()) // Configure security context (e.g., JWT token)
//                .formLogin().disable() // Not applicable for reactive security (consider alternatives for form-based login)
//                .httpBasic().disable(); // Not recommended for production; consider JWT-based authentication;
//
//        return http.build();
//    }
//
//    private Mono<Void> securityContextRepository() {
//        // Implement security context repository based on your chosen authentication mechanism (e.g., JWT)
//        // This example is a placeholder; replace with your specific implementation
//        return Mono.empty();
//    }
//}