package com.epicure.project.service;

import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dto.request.MealRequest;
import com.epicure.project.dto.response.MealInfoResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface MealService {

    ResponseEntity<MessageResponse> create(MealRequest request);
    PageResponse<MealInfoResponse> getMeals(int page, int size);
    ResponseEntity<MealInfoResponse> findMealById(Long mealId);
    ResponseEntity<MessageResponse> update(Long mealId, MealRequest request);
    void delete(Long mealId);
    UserEntity getCurrentUser();
}
