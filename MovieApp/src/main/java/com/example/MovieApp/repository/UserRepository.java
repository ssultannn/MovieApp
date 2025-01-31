package com.example.MovieApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MovieApp.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByUsername(String username);

	UserEntity findByEmail(String mail);

	Long findUserIdByUsername(String username);



}
