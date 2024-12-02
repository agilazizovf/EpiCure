package com.epicure.project.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "Order time must not be null")
    private LocalDateTime orderTime;

    @Positive(message = "Total price must be greater than 0")
    private double totalPrice;

    @NotNull(message = "Table ID must not be null")
    private Long tableId;

    @NotNull(message = "Waiter ID must not be null")
    private Long waiterId;

    @NotNull(message = "Meal IDs must not be null")
    private List<Long> mealIds;
}
