package com.example.persistance.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Cacheable
@Table(name = "patient")
public class PatientDB extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private String sex;

}

