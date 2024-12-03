package com.epicure.project.service;

import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.model.dto.request.TableRequest;
import com.epicure.project.model.dto.response.MessageResponse;
import com.epicure.project.model.dto.response.PageResponse;
import com.epicure.project.model.dto.response.TableInfoResponse;
import org.springframework.http.ResponseEntity;

public interface TableService {

    ResponseEntity<MessageResponse> create(TableRequest request);
    PageResponse<TableInfoResponse> getTables(int page, int size);
    ResponseEntity<TableInfoResponse> findTableById(Long tableId);
    ResponseEntity<MessageResponse> update(Long tableId, TableRequest request);
    void delete(Long tableId);
    UserEntity getCurrentUser();
}
