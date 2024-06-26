package com.mangarider.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_user_credentials")
public class UserCredentials implements UserDetails {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @MapsId
    private User user;

    @Email
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "t_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.ORDINAL)
    private List<UserRole> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
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
}
