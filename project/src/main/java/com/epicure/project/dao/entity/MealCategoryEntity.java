package com.epicure.project.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meal_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
