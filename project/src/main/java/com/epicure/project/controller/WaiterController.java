package com.epicure.project.controller;

import com.epicure.project.dto.request.LoginRequest;
import com.epicure.project.dto.request.WaiterRequest;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import com.epicure.project.dto.response.WaiterInfoResponse;
import com.epicure.project.service.WaiterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/waiters")
@RequiredArgsConstructor
public class WaiterController {

    private final WaiterService waiterService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registration(@RequestBody @Valid WaiterRequest request) {
        return waiterService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(waiterService.login(request));
    }

    @GetMapping("/get-all")
    public PageResponse<WaiterInfoResponse> getWaiters(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return waiterService.getWaiters(page, size);
    }
}
