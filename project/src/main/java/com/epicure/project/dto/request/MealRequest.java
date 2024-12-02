package com.epicure.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MealRequest {

    @Size(min = 1, max = 20, message = "Name must be between 1 and 20 characters")
    @NotEmpty(message = "Name must be not empty")
    @NotBlank(message = "Name is required")
    private String name;

    @Size(min = 1, max = 20, message = "Composition must be between 1 and 20 characters")
    @NotEmpty(message = "Composition must be not empty")
    @NotBlank(message = "Composition is required")
    private String composition;

    private String image;

    @Size(min = 1, max = 20, message = "Size must be between 1 and 20 characters")
    @NotEmpty(message = "Size must be not empty")
    @NotBlank(message = "Size is required")
    private String size;

    private double price;

    private Long categoryId;
}
