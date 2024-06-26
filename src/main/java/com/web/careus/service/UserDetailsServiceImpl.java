package com.web.careus.service;

import com.web.careus.model.user.User;
import com.web.careus.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with phoneNumber: " + phoneNumber));

        // Ubah pengguna menjadi UserDetailsImpl jika ditemukan
        return UserDetailsImpl.build(user);
    }
}