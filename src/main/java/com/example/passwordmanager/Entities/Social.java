package com.example.passwordmanager.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Social extends Password {

    @Column(length = 300)
    private String password;

    @Column(length = 50)
    private String verticationCode;


}
