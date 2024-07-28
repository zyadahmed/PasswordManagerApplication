package com.example.passwordmanager.Dto;

import com.example.passwordmanager.Entities.Social;
import lombok.Data;

@Data
public class SocialPasswordDTO {
    private String name;
    private String description;
    private String password;
    private String verticationCode;

    public SocialPasswordDTO() {
    }

    public SocialPasswordDTO(Social social) {
        this.name = social.getName();
        this.description = social.getDescription();
        this.password = social.getPassword();
        this.verticationCode = social.getVerticationCode();
    }
}
