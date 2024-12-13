package com.paymentauth.service;

import com.paymentauth.enums.Role;
import com.paymentauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.paymentauth.entity.User user = userRepository.findByUsername(username);

        return new User(
                user.getUsername(), user.getPassword(), (Collection<? extends GrantedAuthority>) getAuthorities(user.getRoles()));
    }

    private GrantedAuthority getAuthorities(Role roles) {
        return new SimpleGrantedAuthority("ROLE_" + roles.toString());
    }
}
