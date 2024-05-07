package com.mangarider.security;

import com.mangarider.exception.GlobalServiceException;
import com.mangarider.repository.UserCredentialsRepository;
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

import javax.crypto.SecretKey;

import static com.mangarider.model.entity.UserRole.ADMIN;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter filter,
                                                   AuthenticationProvider provider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> {
                    config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(config -> {
                    config.requestMatchers(POST, "/api/v1/users/registration").permitAll();
                    config.requestMatchers(POST, "/api/v1/users/login").permitAll();
                    config.anyRequest().hasRole(ADMIN.name());
                });
        return http.build();
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
