package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.MealCategoryEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.repository.MealCategoryRepository;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.dto.exception.AuthenticationException;
import com.epicure.project.dto.exception.ResourceNotFoundException;
import com.epicure.project.dto.request.MealCategoryRequest;
import com.epicure.project.dto.response.MealCategoryInfoResponse;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import com.epicure.project.mapper.MealCategoryMapper;
import com.epicure.project.service.MealCategoryService;
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
public class MealCategoryServiceImpl implements MealCategoryService {

    private final UserRepository userRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<MessageResponse> create(MealCategoryRequest request) {
        UserEntity user = getCurrentUser();

        MealCategoryEntity category = new MealCategoryEntity();
        modelMapper.map(request, category);
        category.setUser(user);

        mealCategoryRepository.save(category);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public PageResponse<MealCategoryInfoResponse> getCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MealCategoryEntity> mealCategoryEntities = mealCategoryRepository.findAll(pageable);

        List<MealCategoryInfoResponse> mealInfoResponses = mealCategoryEntities
                .stream()
                .map(MealCategoryMapper::toMealCategoryDTO)
                .collect(Collectors.toList());

        PageResponse<MealCategoryInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(mealInfoResponses);
        pageResponse.setPage(mealCategoryEntities.getPageable().getPageNumber());
        pageResponse.setSize(mealCategoryEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(mealCategoryEntities.getTotalElements());
        pageResponse.setTotalPages(mealCategoryEntities.getTotalPages());
        pageResponse.setLast(mealCategoryEntities.isLast());
        pageResponse.setFirst(mealCategoryEntities.isFirst());

        return pageResponse;
    }

    @Override
    public ResponseEntity<MealCategoryInfoResponse> findCategoryById(Long categoryId) {
        UserEntity user = getCurrentUser();
        MealCategoryEntity mealCategory = mealCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        MealCategoryInfoResponse response = new MealCategoryInfoResponse();
        response.setId(mealCategory.getId());
        response.setName(mealCategory.getName());
        response.setDescription(mealCategory.getDescription());
        response.setImage(mealCategory.getImage());
        response.setAdmin(user.getAdmin());

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> update(Long categoryId, MealCategoryRequest request) {
        UserEntity user = getCurrentUser();
        MealCategoryEntity mealCategory = mealCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!mealCategory.getUser().equals(user)) {
            throw new AuthenticationException("You are not authorized to update this category.");
        }

        mealCategory.setName(request.getName());
        mealCategory.setDescription(request.getDescription());
        mealCategory.setImage(request.getImage());
        mealCategory.setUser(user);
        mealCategoryRepository.save(mealCategory);

        MessageResponse response = new MessageResponse();
        response.setMessage("Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public void delete(Long categoryId) {
        UserEntity user = getCurrentUser();
        MealCategoryEntity mealCategory = mealCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        mealCategoryRepository.deleteById(categoryId);

        if (!mealCategory.getUser().equals(user)) {
            throw new AuthenticationException("You are not authorized to delete this category.");
        }

        mealCategoryRepository.deleteById(categoryId);
    }

    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User not found"));
    }
}
