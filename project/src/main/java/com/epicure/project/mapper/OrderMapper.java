package com.epicure.project.mapper;

import com.epicure.project.dao.entity.OrderEntity;
import com.epicure.project.dto.response.OrderInfoResponse;

public class OrderMapper {

    public static OrderInfoResponse toOrderDTO(OrderEntity entity) {
        OrderInfoResponse response = new OrderInfoResponse();
        response.setId(entity.getId());
        response.setOrderTime(entity.getOrderTime());
        response.setTotalPrice(entity.getTotalPrice());
        response.setStatus(entity.getStatus().toString());

        // Set table and waiter
        if(entity.getTable() != null) {
            response.setTable(entity.getTable());
        }

        if(entity.getWaiter() != null) {
            response.setWaiter(entity.getWaiter());
        }

        // Set meals
        if (entity.getMeals() != null) {
            response.setMeals(entity.getMeals());
        }

        return response;
    }
}
