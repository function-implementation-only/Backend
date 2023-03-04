package com.example.chatservice.config.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {



    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

//        UserDetailsImpl userDetails = new UserDetailsImpl(account);
//        userDetails.setAccount(account);
        return new UserDetailsImpl(account);
    }
}
