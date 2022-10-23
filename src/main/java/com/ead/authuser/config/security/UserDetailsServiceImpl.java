package com.ead.authuser.config.security;

import com.ead.authuser.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        var userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(userModel);
    }

    public UserDetails loadUserById(final UUID id) throws UsernameNotFoundException {
        var userModel = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + id));
        return UserDetailsImpl.build(userModel);
    }

}
