package com.epicure.project.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String composition;

    private String image;

    private String size;

    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private MealCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;
}
