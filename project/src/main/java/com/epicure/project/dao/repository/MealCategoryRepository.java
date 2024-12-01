package com.epicure.project.dao.repository;

import com.epicure.project.dao.entity.MealCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealCategoryRepository extends JpaRepository<MealCategoryEntity, Long> {

    Page<MealCategoryEntity> findAll(Pageable pageable);
}
