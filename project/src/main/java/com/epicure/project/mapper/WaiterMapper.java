package com.epicure.project.mapper;

import com.epicure.project.dao.entity.WaiterEntity;
import com.epicure.project.dto.response.WaiterInfoResponse;

public class WaiterMapper {

    public static WaiterInfoResponse toWaiterDTO(WaiterEntity waiterEntity) {
        WaiterInfoResponse response = new WaiterInfoResponse();
        response.setId(waiterEntity.getId());
        response.setUsername(waiterEntity.getUsername());
        response.setEmail(waiterEntity.getEmail());
        response.setHireDate(waiterEntity.getHireDate());
        response.setUpdateDate(waiterEntity.getUpdateDate());
        return response;
    }
}
