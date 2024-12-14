package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.User;



public interface UserRepo extends JpaRepository<User,Long>{
    
    User save(User s) ;

    Optional<User> findByEmail(String email);

}
