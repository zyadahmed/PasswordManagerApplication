package com.example.passwordmanager.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(length = 30)
    private String name;

    @Column(length = 300)
    private String description;





}
