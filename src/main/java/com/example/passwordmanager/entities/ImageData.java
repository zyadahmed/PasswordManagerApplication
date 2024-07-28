package com.example.passwordmanager.entities;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
@Builder
@Entity
@Table(name = "ImageData")
@Data

public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;

    private String type;
    @Column(name="imagedata",length = 1000)
    private byte[]imageData;
}
