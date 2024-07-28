package com.example.passwordmanager.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Social extends Password {

    @Column(length = 300)
    private String password;

    @Column(length = 50)
    private String verticationCode;


}
