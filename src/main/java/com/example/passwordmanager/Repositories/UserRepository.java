package com.example.passwordmanager.Repositories;

import com.example.passwordmanager.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("UPDATE User u SET u.password = :password WHERE u = :user")
    void updatePassword(@Param("user") User user, @Param("password") String password);


}
