package com.epicure.project.service;

import com.epicure.project.model.dto.request.OrderRequest;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.OrderCheckResponse;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<MessageResponse> create(OrderRequest request);
    ResponseEntity<MessageResponse> acceptOrder(Long orderId);
    ResponseEntity<MessageResponse> rejectOrder(Long orderId);
    ResponseEntity<MessageResponse> serveOrder(Long orderId);
    ResponseEntity<MessageResponse> completeOrder(Long orderId);
    ResponseEntity<OrderCheckResponse> checkOrder(Long orderId);
}
