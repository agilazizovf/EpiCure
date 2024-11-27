package com.epicure.project.dao.repository;

import com.epicure.project.dao.entity.WaiterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaiterRepository extends JpaRepository<WaiterEntity, Long> {
}
