package com.epicure.project.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminResponse {

    private Long id;

    private String username;

    private String email;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;
}
