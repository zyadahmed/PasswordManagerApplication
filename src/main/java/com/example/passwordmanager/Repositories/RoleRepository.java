package com.example.passwordmanager.Repositories;

import com.example.passwordmanager.Entities.Role;
import com.example.passwordmanager.Entities.RoleNames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByname(RoleNames name);
}
