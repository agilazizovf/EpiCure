package com.epicure.project.model.dto.response;

import com.epicure.project.dao.entity.AdminEntity;
import com.epicure.project.dao.entity.MealCategoryEntity;
import lombok.Data;

@Data
public class MealInfoResponse {

    private Long id;

    private String name;

    private String composition;

    private String image;

    private String size;

    private double price;

    private AdminEntity admin;

    private MealCategoryEntity category;
}
