package com.example.persistance.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity()
@Table(name = "diagnosis")
public class DiagnosisDB extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "patient_id")
    private PatientDB patient;

    @ManyToOne()
    @JoinColumn(name = "staff_id")
    private StaffDB staff;

    @Column(nullable = false)
    private String diagnosis;

    @Column(nullable = false, name = "date_time")
    @CreationTimestamp
    private LocalDateTime dateTime;

}
