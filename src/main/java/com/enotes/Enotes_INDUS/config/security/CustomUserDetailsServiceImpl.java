package com.enotes.Enotes_INDUS.config.security;

import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import com.enotes.Enotes_INDUS.service.impl.feature.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private UserCacheService userCacheService;  // ← separate bean, AOP works correctly

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUser(username);
    }

    public CustomUserDetails loadUser(String username) {
        // ← Goes through Spring proxy → @Cacheable works
        CustomUserDetails userDetails = userCacheService.loadAndCacheUser(username);
        userDetails.setUserRepository(userRepository);  // ← inject repo after cache load
        return userDetails;
    }
}
