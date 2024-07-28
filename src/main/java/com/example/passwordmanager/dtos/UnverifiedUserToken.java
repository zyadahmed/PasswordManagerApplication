package com.example.passwordmanager.dtos;

import com.example.passwordmanager.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnverifiedUserToken {
    private Instant date;
    private User user;

}
