package com.enotes.Enotes_INDUS.config.security;

import com.enotes.Enotes_INDUS.model.User;
import com.enotes.Enotes_INDUS.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
             Optional<User> userOptional=userRepo.findByEmail(username);
             User user=userOptional.get();

             if(user==null){
                 throw  new UsernameNotFoundException("Invalid email");
             }


             return new CustomUserDetails(user);

    }
}
