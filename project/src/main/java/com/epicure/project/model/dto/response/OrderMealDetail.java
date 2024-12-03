package com.epicure.project.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderMealDetail {
    private double price;
    private long quantity;
}
