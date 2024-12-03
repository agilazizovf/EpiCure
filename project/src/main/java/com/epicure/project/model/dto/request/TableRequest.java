package com.epicure.project.model.dto.request;

import lombok.Data;

@Data
public class TableRequest {

    private int tableNumber;
    private int capacity;
    private boolean isOccupied;
}
