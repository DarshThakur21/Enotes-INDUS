package com.enotes.Enotes_INDUS.config.security;
import com.enotes.Enotes_INDUS.model.User;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public CustomUserDetails(User user) {
        super();
        this.user=user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthority=new ArrayList<SimpleGrantedAuthority>();
        user.getRole().forEach(role -> {
            simpleGrantedAuthority.add(new SimpleGrantedAuthority(role.getRole()));
        });

        return simpleGrantedAuthority;
    }

    @Override
    public String getPassword() {
    return user.getPassword();
    }

    @Override
    public String getUsername() {
//        return "";
    return user.getEmail();
    }
}
