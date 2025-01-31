package com.example.MovieApp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
        
        // Запрос для получения информации о пользователе
        jdbcDao.setUsersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?");
        
        // Запрос для получения ролей пользователя
        jdbcDao.setAuthoritiesByUsernameQuery("SELECT username, role FROM user_roles WHERE username = ?");
        
        return jdbcDao;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        // Здесь можно добавить энкодер пароля, если требуется
        // authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	return http.csrf().disable()
    		    .authorizeHttpRequests()
    		    .requestMatchers(HttpMethod.POST, "/movies").hasRole("ADMIN")
    		    .requestMatchers(HttpMethod.GET, "/movies/**").hasAnyRole("USER", "ADMIN")
    		    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // Разрешить доступ к Swagger UI и документации
    		    .requestMatchers("/h2-console/**").permitAll()  // Разрешить доступ к H2 консоли
    		    .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
    		    .and()
    		    .httpBasic()  // Включение базовой аутентификации
    		    .and()
    		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Использование Stateless сессий
    		    .and()
    		    .headers().frameOptions().disable()  // Разрешить фреймы для H2 консоли
    		    .and()
    		    .build();
    }
}
