package com.epicure.project.service.impl;

import com.epicure.project.dao.entity.TableEntity;
import com.epicure.project.dao.entity.UserEntity;
import com.epicure.project.dao.repository.TableRepository;
import com.epicure.project.dao.repository.UserRepository;
import com.epicure.project.dto.exception.AuthenticationException;
import com.epicure.project.dto.exception.ResourceNotFoundException;
import com.epicure.project.dto.request.TableRequest;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import com.epicure.project.dto.response.TableInfoResponse;
import com.epicure.project.mapper.TableMapper;
import com.epicure.project.service.TableService;
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
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<MessageResponse> create(TableRequest request) {
        UserEntity user = getCurrentUser();

        TableEntity table = new TableEntity();
        modelMapper.map(request, table);
        table.setAdmin(user.getAdmin());
        tableRepository.save(table);

        MessageResponse response = new MessageResponse();
        response.setMessage("Created Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public PageResponse<TableInfoResponse> getTables(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TableEntity> tableEntities = tableRepository.findAll(pageable);

        List<TableInfoResponse> tableInfoResponses = tableEntities
                .stream()
                .map(TableMapper::toTableDTO)
                .collect(Collectors.toList());

        PageResponse<TableInfoResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(tableInfoResponses);
        pageResponse.setPage(tableEntities.getPageable().getPageNumber());
        pageResponse.setSize(tableEntities.getPageable().getPageSize());
        pageResponse.setTotalElements(tableEntities.getTotalElements());
        pageResponse.setTotalPages(tableEntities.getTotalPages());
        pageResponse.setLast(tableEntities.isLast());
        pageResponse.setFirst(tableEntities.isFirst());

        return pageResponse;
    }

    @Override
    public ResponseEntity<TableInfoResponse> findTableById(Long tableId) {
        UserEntity user = getCurrentUser();
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        TableInfoResponse response = new TableInfoResponse();
        modelMapper.map(table, response);
        table.setAdmin(user.getAdmin());

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @Override
    public ResponseEntity<MessageResponse> update(Long tableId, TableRequest request) {
        UserEntity user = getCurrentUser();

        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        if (!table.getAdmin().equals(user.getAdmin())) {
            throw new AuthenticationException("You are not authorized to update this table.");
        }
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setOccupied(request.isOccupied());
        table.setAdmin(user.getAdmin());

        tableRepository.save(table);

        MessageResponse response = new MessageResponse();
        response.setMessage("Updated Successfully");
        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long tableId) {
        UserEntity user = getCurrentUser();

        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        if (!table.getAdmin().equals(user.getAdmin())) {
            throw new AuthenticationException("You are not authorized to delete this table.");
        }
        tableRepository.deleteById(tableId);
    }

    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
