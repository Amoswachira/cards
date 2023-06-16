package com.cards.cards.service;

import com.cards.cards.config.PasswordEncoderConfig;
import com.cards.cards.exception.NotFoundException;
import com.cards.cards.exception.UnauthorizedException;
import com.cards.cards.model.User;
import com.cards.cards.repository.UserRepository;
import com.cards.cards.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User createUser(User user) {
        // You can add additional validations here
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return user;
    }

    public String generateToken(User user) {
        // Generate JWT token using jwtTokenProvider
        return jwtTokenProvider.generateToken(user);
    }
}
