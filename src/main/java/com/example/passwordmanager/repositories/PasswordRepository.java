package com.example.passwordmanager.repositories;

import com.example.passwordmanager.entities.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password,Integer> {



}
