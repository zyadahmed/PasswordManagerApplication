package com.example.passwordmanager.repositories;

import com.example.passwordmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("UPDATE User u SET u.password = :password WHERE u = :user")
    void updatePassword(@Param("user") User user, @Param("password") String password);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.passwordList p WHERE u.email = :userEmail")
    Optional<User> findUserWithPasswords(@Param("userEmail") String userEmail);



}
