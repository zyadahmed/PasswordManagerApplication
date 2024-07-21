package com.example.passwordmanager.Repositories;

import com.example.passwordmanager.Entities.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password,Integer> {

}
