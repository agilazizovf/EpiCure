package com.epicure.project.service;

import com.epicure.project.dto.request.LoginRequest;
import com.epicure.project.dto.request.WaiterRequest;
import com.epicure.project.dto.response.JwtAuthenticationResponse;
import com.epicure.project.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface WaiterService {

    ResponseEntity<MessageResponse> register(WaiterRequest request);
    JwtAuthenticationResponse login(LoginRequest request);
}
