package com.example.persistance.entity;

import com.example.util.enums.Role;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "staff")
public class StaffDB extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    @Column
    private Role role;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private String sex;

}
