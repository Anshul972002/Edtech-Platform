package com.example.edtech.config;

import com.example.edtech.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Collections;

//@AllArgsConstructor

@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private String id;
    private String email;
    private String password;
    private String role;  // Changed to String to match UserEntity
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountLocked;
    private boolean enabled;

//     Constructor that matches the create method call
    public UserPrincipal(String id, String email, String password, String role, Collection<? extends GrantedAuthority> authorities
    ,boolean accountLocked,boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.accountLocked=accountLocked;
        this.enabled=enabled;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Using email as username
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

    public static UserPrincipal create(UserEntity user) {
        // Since role is a String, create a single authority
        List<GrantedAuthority> authorities = (user.getRole() != null)?Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())):Collections.emptyList();

        return new UserPrincipal(
                user.getId().toHexString(),  // Convert ObjectId to String
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                authorities,
                user.isAccountLocked(),
                user.isEnabled()
        );
    }
}