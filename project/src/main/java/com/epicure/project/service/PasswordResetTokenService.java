package com.epicure.project.service;

public interface PasswordResetTokenService {

    void initiatePasswordReset(String email);
    void resetPassword(String token, String newPassword);
}
