package com.epicure.project.mapper;

import com.epicure.project.dao.entity.MealEntity;
import com.epicure.project.dto.response.MealInfoResponse;

public class MealMapper {

    public static MealInfoResponse toMealDTO(MealEntity entity) {
        MealInfoResponse response = new MealInfoResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setImage(entity.getImage());
        response.setComposition(entity.getComposition());
        response.setSize(entity.getSize());
        response.setPrice(entity.getPrice());

        // Set admin if it exists
        if (entity.getUser() != null && entity.getUser().getAdmin() != null) {
            response.setAdmin(entity.getUser().getAdmin());
        }

        if (entity.getCategory() != null) {
            response.setCategory(entity.getCategory());
        }
        return response;
    }
}
