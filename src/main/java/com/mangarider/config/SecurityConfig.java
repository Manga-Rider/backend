package com.mangarider.config;

import com.mangarider.exception.GlobalServiceException;
import com.mangarider.repository.UserCredentialsRepository;
import com.mangarider.security.JwtAuthenticationFilter;
import com.mangarider.security.properties.SecurityProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;

import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter filter,
                                                   AuthenticationProvider provider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> {
                    config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(config -> {
                    config.requestMatchers("/swagger-ui/**").permitAll(); // for open api
                    config.requestMatchers("/swagger-ui.html").permitAll();
                    config.requestMatchers("/v3/api-docs/**").permitAll();
                    config.requestMatchers(POST, "/api/v1/users/registration").permitAll();
                    config.requestMatchers(POST, "/api/v1/users/login").permitAll();
                    config.requestMatchers("/api/v1/test/**").permitAll();
                    config.anyRequest().authenticated();
                });
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService service, PasswordEncoder encoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(service);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(UserCredentialsRepository repository) {
        return (login) -> repository.findByEmail(login)
                .orElseThrow(() -> new GlobalServiceException(
                        HttpStatus.UNAUTHORIZED,
                        "user with login = {%s} not found".formatted(login)
                ));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey signingKey(SecurityProperties properties) {
        byte[] keyBytes = Decoders.BASE64.decode(properties.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
