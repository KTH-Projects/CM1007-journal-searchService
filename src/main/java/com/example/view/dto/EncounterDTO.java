package com.example.view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncounterDTO {
    private String id;
    private String staffID;
    private String patientID;
    private LocalDateTime date;
}
