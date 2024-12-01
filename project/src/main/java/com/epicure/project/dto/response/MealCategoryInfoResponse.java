package com.epicure.project.dto.response;

import com.epicure.project.dao.entity.AdminEntity;
import lombok.Data;

@Data
public class MealCategoryInfoResponse {

    private Long id;

    private String name;

    private String description;

    private String image;

    private AdminEntity admin;

}
