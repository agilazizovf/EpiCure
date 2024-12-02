package com.epicure.project.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private AdminEntity admin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<MealCategoryEntity> category;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<MealEntity> meals;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private WaiterEntity waiter;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<AuthorityEntity> authorities = new HashSet<>();

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }




}
