package com.example.passwordmanager.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role",columnDefinition = "varchar(50) default 'USER'")
    @Enumerated(EnumType.STRING)
    RoleNames name;
}
