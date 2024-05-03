package com.example.passwordmanager.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(length = 300)
    private String password;
    @Column
    private Date passCreationDate;
    @OneToOne(optional = false)
    @JoinColumn(name = "roleId")
    private Role Role;

}
