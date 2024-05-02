package com.example.passwordmanager.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
public class Payment extends Password {


    @Column(length = 60)
    private String number;
    private Integer cvv;
    private Date expirationDate;
}
