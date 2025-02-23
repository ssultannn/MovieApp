package com.example.MovieApp.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.MovieApp.security.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtFilter jwtFilter; // Ваш JWT фильтр

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
        return jdbcDao;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder()); // Используем наш кастомный encoder
        return authProvider;
    }

    // Настройка DelegatingPasswordEncoder для обработки паролей с {noop}
    @Bean
    public PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        
        // Добавляем NoOpPasswordEncoder
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        
        // Также можно добавить другие энкодеры, например BCrypt, для будущего использования
     

        // Создаём DelegatingPasswordEncoder с {noop} как дефолтным префиксом
        return new DelegatingPasswordEncoder("noop", encoders);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(authenticationProvider());
        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests(auth -> auth
                // Разрешаем регистрацию и логин
                .requestMatchers(HttpMethod.POST, "/users/register/admin", "/users/register/user", "/users/login").permitAll()
                // Разрешаем OPTIONS-запросы
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Разрешаем доступ к swagger endpoints
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/watchlist/**").permitAll()
                // Остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            }))
            .httpBasic(); // Для тестирования basic auth

        // Добавляем JWT фильтр в цепочку
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}