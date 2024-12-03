package com.epicure.project.controller;

import com.epicure.project.model.dto.request.MealRequest;
import com.epicure.project.model.dto.response.MealInfoResponse;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.PageResponse;
import com.epicure.project.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid MealRequest request) {
        return mealService.create(request);
    }

    @GetMapping("/get-all")
    public PageResponse<MealInfoResponse> getMeals(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return mealService.getMeals(page, size);
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealInfoResponse> findMealById(@PathVariable Long mealId) {
        return mealService.findMealById(mealId);
    }

    @PutMapping("/update/{mealId}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long mealId, MealRequest request) {
        return mealService.update(mealId, request);
    }

    @DeleteMapping("/delete/{mealId}")
    public void delete(@PathVariable Long mealId) {
        mealService.delete(mealId);
    }
}
