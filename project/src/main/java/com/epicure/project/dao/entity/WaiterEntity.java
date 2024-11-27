package com.epicure.project.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiters")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaiterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    private LocalDateTime hireDate;
    private LocalDateTime updateDate;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;
}
