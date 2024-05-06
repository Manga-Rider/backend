package com.mangarider.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "t_user_credentials")
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID credentialsId;

    @Email
    private String email;

    private String password;

    @ElementCollection
    @Enumerated(EnumType.ORDINAL)
    private List<UserRole> roles = new ArrayList<>();
}
