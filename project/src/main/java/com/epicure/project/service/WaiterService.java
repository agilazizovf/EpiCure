package com.epicure.project.service;

import com.epicure.project.model.dto.request.LoginRequest;
import com.epicure.project.model.dto.request.WaiterRequest;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.PageResponse;
import com.epicure.project.model.dto.response.WaiterInfoResponse;
import org.springframework.http.ResponseEntity;


public interface WaiterService {

    ResponseEntity<MessageResponse> register(WaiterRequest request);
    ResponseEntity<?> login(LoginRequest request);

    PageResponse<WaiterInfoResponse> getWaiters(int page, int size);
}
