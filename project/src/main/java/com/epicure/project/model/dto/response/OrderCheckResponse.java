package com.epicure.project.model.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class OrderCheckResponse {
    private int tableNumber;
    private double totalPrice;
    private Map<String, OrderMealDetail> mealDetails;
}
