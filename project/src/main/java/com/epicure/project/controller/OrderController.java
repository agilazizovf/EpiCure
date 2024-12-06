package com.epicure.project.controller;

import com.epicure.project.model.dto.request.OrderRequest;
import com.epicure.project.model.dto.response.IncomeReportResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.OrderCheckResponse;
import com.epicure.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createOrder(@RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @PutMapping("/{orderId}/accept")
    public ResponseEntity<MessageResponse> acceptOrder(@PathVariable Long orderId) {
        return orderService.acceptOrder(orderId);
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<MessageResponse> rejectOrder(@PathVariable Long orderId) {
        return orderService.rejectOrder(orderId);
    }

    @PutMapping("/{orderId}/serve")
    public ResponseEntity<MessageResponse> serveOrder(@PathVariable Long orderId) {
        return orderService.serveOrder(orderId);
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<MessageResponse> completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @GetMapping("/{orderId}/check")
    public ResponseEntity<OrderCheckResponse> checkOrder(@PathVariable Long orderId) {
        return orderService.checkOrder(orderId);
    }

    @GetMapping("/income-report")
    public ResponseEntity<IncomeReportResponse> getIncomeReport() {
        return ResponseEntity.ok(orderService.getIncomeReport());
    }

    @DeleteMapping("/delete/{orderId}")
    public void delete(@PathVariable Long orderId) {
        orderService.delete(orderId);
    }
}
