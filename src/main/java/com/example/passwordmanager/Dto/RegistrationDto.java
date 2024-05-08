package com.example.passwordmanager.Dto;


import com.example.passwordmanager.Entities.RoleNames;
import lombok.Data;

@Data
public class RegistrationDto {
    private String name;
    private String email;
    private String password;
    private RoleNames role;

}
