package com.epicure.project.service;

import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dto.request.MealCategoryRequest;
import com.epicure.project.dto.response.MealCategoryInfoResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import org.springframework.http.ResponseEntity;

public interface MealCategoryService {

    ResponseEntity<MessageResponse> create(MealCategoryRequest request);
    PageResponse<MealCategoryInfoResponse> getCategories(int page, int size);
    ResponseEntity<MealCategoryInfoResponse> findCategoryById(Long categoryId);

    ResponseEntity<MessageResponse> update(Long categoryId, MealCategoryRequest request);

    void delete(Long categoryId);

    UserEntity getCurrentUser();
}
