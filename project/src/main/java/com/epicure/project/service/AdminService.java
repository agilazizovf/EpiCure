package com.epicure.project.service;

import com.epicure.project.model.dto.request.AdminRequest;
import com.epicure.project.model.dto.request.LoginRequest;
import com.epicure.project.model.dto.response.IncomeReportResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<MessageResponse> register(AdminRequest request);

    ResponseEntity<?> login(LoginRequest request);
}
