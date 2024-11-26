package com.modexa.carelink.service;

import com.modexa.carelink.dto.auth.AuthResponse;
import com.modexa.carelink.dto.auth.LoginRequest;
import com.modexa.carelink.dto.auth.RegisterRequest;
import com.modexa.carelink.exception.AuthenticationException;
import com.modexa.carelink.model.User;
import com.modexa.carelink.repository.UserRepository;
import com.modexa.carelink.security.JwtService;
import com.modexa.carelink.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email already exists");
        }

        String verificationToken = UUID.randomUUID().toString();

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .contactNumber(request.getContactNumber())
                .role(request.getRole())
                .professionalTitle(request.getProfessionalTitle())
                .licenseNumber(request.getLicenseNumber())
                .emailVerified(false)
                .verificationToken(verificationToken)
                .build();

        var savedUser = userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .user(savedUser)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), // Use the actual username for authentication
                        request.getPassword()
                )
        );

        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .user(user)
                .build();
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void forgotPassword(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        throw new UnsupportedOperationException("Password reset via email is not implemented yet");
    }

    public void resetPassword(String token, String newPassword) {
        throw new UnsupportedOperationException("Password reset via token is not implemented yet");
    }

    public void verifyEmail(String token) {
        var user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public void resendVerificationEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getEmailVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
    }
}
