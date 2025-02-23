package com.example.MovieApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.MovieApp.entity.UserEntity;
import com.example.MovieApp.entity.UserRolesEntity;
import com.example.MovieApp.repository.UserRepository;
import com.example.MovieApp.repository.UserRolesRepository;
import com.example.MovieApp.request.UserLoginRequest;
import com.example.MovieApp.request.UserRegisterRequest;
import com.example.MovieApp.security.JwtUtil;

@Service
public class UserService {

	   @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private UserRolesRepository userRolesRepository;

	    @Autowired
	    private JwtUtil jwtUtil;

	public ResponseEntity<String> registerAsUser(UserRegisterRequest user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			return ResponseEntity.status(409).body("Username already exists!");
		}

		if (userRepository.findByEmail(user.getEmail()) != null) {
			return ResponseEntity.status(409).body("Email already exists!");
		}

		// Добавляем префикс "{noop}" к паролю
		user.setPassword("{noop}" + user.getPassword());

		UserEntity newUser = new UserEntity();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(user.getPassword());
		newUser.setEmail(user.getEmail());
		userRepository.save(newUser);
		UserRolesEntity role = new UserRolesEntity();
		role.setUserId(newUser.getId());
		role.setRole("ROLE_USER");
		userRolesRepository.save(role);
		return ResponseEntity.ok("User registered successfully!");
	}

	public ResponseEntity<String> registerAsAdmin(UserRegisterRequest user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			return ResponseEntity.status(409).body("Username already exists!");
		}

		if (userRepository.findByEmail(user.getEmail()) != null) {
			return ResponseEntity.status(409).body("Email already exists!");
		}

		// Добавляем префикс "{noop}" к паролю
		user.setPassword("{noop}" + user.getPassword());

		UserEntity newUser = new UserEntity();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(user.getPassword());
		newUser.setEmail(user.getEmail());
		userRepository.save(newUser);
		UserRolesEntity role = new UserRolesEntity();
		role.setUserId(newUser.getId());
		role.setRole("ROLE_ADMIN");
		userRolesRepository.save(role);
		return ResponseEntity.ok("User registered successfully!");
	}


     public ResponseEntity<String> login(UserLoginRequest user) {
        UserEntity foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser != null && foundUser.getPassword().equals("{noop}" + user.getPassword())) {
            // Извлекаем роли для данного пользователя
            List<UserRolesEntity> rolesEntities = userRolesRepository.findByUserId(foundUser.getId());
            List<String> roles = rolesEntities.stream()
                                              .map(UserRolesEntity::getRole)
                                              .collect(Collectors.toList());
            // Генерируем токен с ролями
            String token = jwtUtil.generateToken(foundUser.getUsername(), roles);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials!");
    }

	public Long findUserIdByUsername(String username) {
		Long userId = userRepository.findUserIdByUsername(username);
		return userId;
	}
}
