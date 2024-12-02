package com.epicure.project.dao.repository;

import com.epicure.project.dao.entity.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    Page<TableEntity> findAll(Pageable pageable);
}
