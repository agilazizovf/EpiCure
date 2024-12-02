package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.MealCategoryEntity;
import com.epicure.project.dao.entity.MealEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.repository.MealCategoryRepository;
import com.epicure.project.dao.repository.MealRepository;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.dto.exception.AuthenticationException;
import com.epicure.project.dto.exception.ResourceNotFoundException;
import com.epicure.project.dto.request.MealRequest;
import com.epicure.project.dto.response.MealInfoResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import com.epicure.project.mapper.MealMapper;
import com.epicure.project.service.MealService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<MessageResponse> create(MealRequest request) {
        UserEntity user = getCurrentUser();
        MealCategoryEntity mealCategory = mealCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        MealEntity meal = new MealEntity();
        modelMapper.map(request, meal);
        meal.setCategory(mealCategory);
        meal.setUser(user);

        mealRepository.save(meal);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public PageResponse<MealInfoResponse> getMeals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MealEntity> mealEntities = mealRepository.findAll(pageable);

        List<MealInfoResponse> mealInfoResponses = mealEntities
                .stream()
                .map(MealMapper::toMealDTO)
                .collect(Collectors.toList());

        PageResponse<MealInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(mealInfoResponses);
        pageResponse.setPage(mealEntities.getPageable().getPageNumber());
        pageResponse.setSize(mealEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(mealEntities.getTotalElements());
        pageResponse.setTotalPages(mealEntities.getTotalPages());
        pageResponse.setLast(mealEntities.isLast());
        pageResponse.setFirst(mealEntities.isFirst());

        return pageResponse;
    }

    @Override
    public ResponseEntity<MealInfoResponse> findMealById(Long mealId) {
        UserEntity user = getCurrentUser();
        MealEntity meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        MealInfoResponse mealInfoResponse = new MealInfoResponse();
        modelMapper.map(meal, mealInfoResponse);
        mealInfoResponse.setAdmin(user.getAdmin());

        return ResponseEntity.status(HttpStatus.FOUND).body(mealInfoResponse);
    }

    @Override
    public ResponseEntity<MessageResponse> update(Long mealId, MealRequest request) {
        UserEntity user = getCurrentUser();
        MealEntity meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));


        if (!meal.getUser().equals(user)) {
            throw new AuthenticationException("You are not authorized to update this meal.");
        }
        meal.setName(request.getName());
        meal.setPrice(request.getPrice());
        meal.setComposition(request.getComposition());
        meal.setSize(request.getSize());
        meal.setImage(request.getImage());
        meal.setCategory(mealCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        meal.setUser(user);

        mealRepository.save(meal);

        MessageResponse response = new MessageResponse();
        response.setMessage("Updated Successfully");

        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long mealId) {
        UserEntity user = getCurrentUser();

        MealEntity meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found"));

        if (!meal.getUser().equals(user)) {
            throw new AuthenticationException("You are not authorized to delete this meal.");
        }
        mealRepository.deleteById(mealId);
    }

    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
