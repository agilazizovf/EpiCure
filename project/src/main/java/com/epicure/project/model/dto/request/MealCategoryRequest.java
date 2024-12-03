package com.epicure.project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MealCategoryRequest {

    @Size(min = 1, max = 20, message = "Name must be between 1 and 20 characters")
    @NotEmpty(message = "Name must be not empty")
    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 300, message = "Description must be maximum 300 characters")
    private String description;

    private String image;
}
