package com.epicure.project.mapper;

import com.epicure.project.dao.entity.TableEntity;
import com.epicure.project.dto.response.TableInfoResponse;

public class TableMapper {

    public static TableInfoResponse toTableDTO(TableEntity entity) {
        TableInfoResponse response = new TableInfoResponse();
        response.setId(entity.getId());
        response.setTableNumber(entity.getTableNumber());
        response.setCapacity(entity.getCapacity());
        response.setOccupied(entity.isOccupied());

        // Set admin if it exists
        if (entity.getAdmin() != null) {
            response.setAdmin(entity.getAdmin());
        }

        return response;
    }
}
