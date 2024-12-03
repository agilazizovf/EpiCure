package com.epicure.project.model.dto.response;

import com.epicure.project.dao.entity.AdminEntity;
import lombok.Data;

@Data
public class TableInfoResponse {

    private Long id;
    private int tableNumber;
    private int capacity;
    private boolean isOccupied;
    private AdminEntity admin;
}
