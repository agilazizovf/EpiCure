package com.epicure.project.dto.response;

import com.epicure.project.dao.entity.MealEntity;
import com.epicure.project.dao.entity.TableEntity;
import com.epicure.project.dao.entity.WaiterEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderInfoResponse {

    private Long id;

    private LocalDateTime orderTime;

    private double totalPrice;

    private String status;

    private TableEntity table;

    private WaiterEntity waiter;

    private List<MealEntity> meals;
}
