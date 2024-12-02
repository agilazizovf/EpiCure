package com.epicure.project.dto.request;

import lombok.Data;

@Data
public class TableRequest {

    private int tableNumber;
    private int capacity;
    private boolean isOccupied;
}
