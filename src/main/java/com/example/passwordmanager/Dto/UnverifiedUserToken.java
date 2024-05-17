package com.example.passwordmanager.Dto;

import com.example.passwordmanager.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnverifiedUserToken {
    private Instant date;
    private User user;

}
