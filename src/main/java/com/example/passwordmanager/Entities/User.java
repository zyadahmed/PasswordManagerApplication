package com.example.passwordmanager.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name is mandatory")
    private String name;

    @Column(unique = true)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(length = 300)
    private String password;
    @Column
    private Date passCreationDate;
    @Enumerated(EnumType.STRING)
    private RoleNames role;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagedata_id", referencedColumnName = "id")
    private ImageData imageData;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private List<Password> passwordList;





    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passCreationDate=" + passCreationDate +
                ", Role=" + role +
                '}';
    }
    @PrePersist
    protected void onCreate() {
        this.passCreationDate = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public void addPassword(Password password) {
        if (passwordList == null) {
            passwordList = new ArrayList<>();
        }
        passwordList.add(password);
    }

    public void removePassword(Password password) {
        if (passwordList != null) {
            passwordList.remove(password);
        }
    }
}
