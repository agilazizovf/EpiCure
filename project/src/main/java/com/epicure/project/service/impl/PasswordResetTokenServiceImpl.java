package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.AdminEntity;
import com.epicure.project.dao.entity.PasswordResetTokenEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.repository.AdminRepository;
import com.epicure.project.dao.repository.PasswordResetTokenRepository;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.model.exception.AuthenticationException;
import com.epicure.project.model.exception.GlobalExceptionHandler;
import com.epicure.project.model.exception.ResourceNotFoundException;
import com.epicure.project.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void initiatePasswordReset(String email) {
        AdminEntity admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + email));

        Optional<UserEntity> user = userRepository.findByUsername(admin.getUsername());
        if (user.isEmpty()) {
            throw new AuthenticationException("User not associated with client: " + email);
        }

        // Delete any existing token for the user
        tokenRepository.deleteByUser_Username(user.get().getUsername());

        // Generate and save a new token with expiration date
        String token = generateAndSaveActivationToken(admin.getEmail());

        // Send the reset email
        sendResetEmail(admin.getEmail(), token);
    }

    private void sendResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, take this token:\n" +token);

        mailSender.send(message);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetTokenEntity resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidParameterException("Invalid token"));

        if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException("Token has expired");
        }

        UserEntity user = userRepository.findByUsername(resetToken.getUser().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Optionally, delete the token after use
        tokenRepository.deleteByToken(token);
    }


    private String generateAndSaveActivationToken(String email) {
        String generatedToken = generateActivationCode();
        PasswordResetTokenEntity token = PasswordResetTokenEntity.builder()
                .token(generatedToken)
                .expirationDate(LocalDateTime.now().plusMinutes(15))  // Set expiration date here
                .user(adminRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found!")).getUser())
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
