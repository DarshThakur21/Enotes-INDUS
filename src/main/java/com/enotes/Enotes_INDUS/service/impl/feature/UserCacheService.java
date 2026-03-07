package com.enotes.Enotes_INDUS.service.impl.feature;

import com.enotes.Enotes_INDUS.config.security.CustomUserDetails;
import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCacheService {

    @Autowired
    private UserRepo userRepository;

    @Cacheable(cacheNames = "UserSecurity", key = "#username")
    public CustomUserDetails loadAndCacheUser(String username) {
        log.info("UserCacheService : Cache MISS - hitting DB for {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }

    @CacheEvict(cacheNames = "UserSecurity", key = "#username")
    public void evictUser(String username) {
        log.info("UserCacheService : evicting cache for {}", username);
    }
}