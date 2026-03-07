package com.enotes.Enotes_INDUS.config.security;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserDetails implements UserDetails {

    @JsonIgnore
    private User user;

    @JsonIgnore
    private UserRepo userRepository;

    private Integer userId;
    private String username;
    private String password;
    private Set<String> roles = new HashSet<>();

    public CustomUserDetails() {
    }

    public CustomUserDetails(User user) {  // ← back to single arg constructor
        this.user = user;
        this.userId = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRole()
                .stream()
                .map(r -> "ROLE_" + r.getRole())
                .collect(Collectors.toSet());
    }

    // ← Called by UserDetailsServiceImpl after loading from Redis
    public void setUserRepository(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        if (user == null && userRepository != null) {
            user = userRepository.findByEmail(this.username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public static String getUsernameForSearch() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}