package com.epicure.project.dao.repository;

import com.epicure.project.dao.entity.MealEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<MealEntity, Long> {

    Page<MealEntity> findAll(Pageable pageable);
}
