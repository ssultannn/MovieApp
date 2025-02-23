package com.example.MovieApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieApp.entity.UserRolesEntity;

public interface UserRolesRepository extends JpaRepository<UserRolesEntity,Long>{

	List<UserRolesEntity> findByUserId(Long id);

}
