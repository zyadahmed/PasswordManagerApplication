package com.example.passwordmanager.dtos;

import com.example.passwordmanager.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class UserDto {
    private int id;
    private String email;
    private String name;
    private Date passCreationDate;

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getUsername();
        this.name = user.getName();
        this.passCreationDate = user.getPassCreationDate();
    }
}
