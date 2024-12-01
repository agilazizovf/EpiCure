package com.epicure.project.controller;

import com.epicure.project.dto.request.MealCategoryRequest;
import com.epicure.project.dto.response.MealCategoryInfoResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import com.epicure.project.service.MealCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meal-categories")
@RequiredArgsConstructor
public class MealCategoryController {

    private final MealCategoryService mealCategoryService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid MealCategoryRequest request) {
        return mealCategoryService.create(request);
    }

    @GetMapping("/get-all")
    public PageResponse<MealCategoryInfoResponse> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return mealCategoryService.getCategories(page, size);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<MealCategoryInfoResponse> findCategoryById(@PathVariable Long categoryId) {
        return mealCategoryService.findCategoryById(categoryId);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long categoryId, MealCategoryRequest request) {
        return mealCategoryService.update(categoryId, request);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        mealCategoryService.delete(categoryId);
    }
}
