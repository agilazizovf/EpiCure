package com.epicure.project.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WaiterInfoResponse {

    private Long id;

    private String username;
    private String email;

    private LocalDateTime hireDate;
    private LocalDateTime updateDate;
}
