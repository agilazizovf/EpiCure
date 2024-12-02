package com.epicure.project.mapper;

import com.epicure.project.dao.entity.MealCategoryEntity;
import com.epicure.project.dto.response.MealCategoryInfoResponse;

public class MealCategoryMapper {

    public static MealCategoryInfoResponse toMealCategoryDTO(MealCategoryEntity entity) {
        MealCategoryInfoResponse response = new MealCategoryInfoResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setImage(entity.getImage());

        // Set admin if it exists
        if (entity.getUser() != null && entity.getUser().getAdmin() != null) {
            response.setAdmin(entity.getUser().getAdmin());
        }

        return response;
    }
}
