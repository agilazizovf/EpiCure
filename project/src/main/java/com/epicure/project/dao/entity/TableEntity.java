package com.epicure.project.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tables")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number", nullable = false, unique = true)
    private int tableNumber;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "is_occupied", nullable = false)
    private boolean isOccupied;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false) // Foreign key to Admin
    @JsonIgnore
    private AdminEntity admin;

}
