package com.epicure.project.controller;

import com.epicure.project.model.dto.request.AdminRequest;
import com.epicure.project.model.dto.request.LoginRequest;
import com.epicure.project.model.dto.response.IncomeReportResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registration(@RequestBody @Valid AdminRequest request) {
        return adminService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }
}
