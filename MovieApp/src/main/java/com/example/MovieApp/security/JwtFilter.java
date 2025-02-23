package com.example.MovieApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
	 @Autowired
	    private JwtUtil jwtUtil;

	    @Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	            throws ServletException, IOException {
	        String authHeader = request.getHeader("Authorization");

	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            String username = jwtUtil.extractUsername(token);

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                // Извлекаем роли из токена
	                List<String> roles = jwtUtil.extractRoles(token); // Нужно создать метод для извлечения ролей

	                if (jwtUtil.validateToken(token)) {
	                    // Создаем объект аутентификации с ролями
	                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
	                            username, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
	                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	                }
	            }
	        }

	        filterChain.doFilter(request, response);
	    }
}
