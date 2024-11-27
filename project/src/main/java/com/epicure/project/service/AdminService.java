package com.epicure.project.service;

import com.epicure.project.dto.request.AdminRequest;
import com.epicure.project.dto.request.LoginRequest;
import com.epicure.project.dto.response.JwtAuthenticationResponse;
import com.epicure.project.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<MessageResponse> register(AdminRequest request);

    JwtAuthenticationResponse login(LoginRequest request);
}
