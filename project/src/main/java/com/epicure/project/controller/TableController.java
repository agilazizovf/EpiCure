package com.epicure.project.controller;

import com.epicure.project.dto.request.TableRequest;
import com.epicure.project.dto.response.MessageResponse;
import com.epicure.project.dto.response.PageResponse;
import com.epicure.project.dto.response.TableInfoResponse;
import com.epicure.project.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid TableRequest request) {
        return tableService.create(request);
    }

    @GetMapping("/get-all")
    public PageResponse<TableInfoResponse> getTables(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return tableService.getTables(page, size);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<TableInfoResponse> findTableById(@PathVariable Long tableId) {
        return tableService.findTableById(tableId);
    }

    @PutMapping("/update/{tableId}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long tableId, TableRequest request) {
        return tableService.update(tableId, request);
    }

    @DeleteMapping("/delete/{tableId}")
    public void delete(@PathVariable Long tableId) {
        tableService.delete(tableId);
    }
}
