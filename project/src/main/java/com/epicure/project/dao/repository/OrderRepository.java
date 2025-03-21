package com.epicure.project.dao.repository;

import com.epicure.project.dao.entity.OrderEntity;
import com.epicure.project.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findAll(Pageable pageable);

    List<OrderEntity> findByStatusAndOrderTimeBetween(
            OrderStatus status,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}
