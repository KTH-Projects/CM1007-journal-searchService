package com.example.core.entity;


import com.example.persistance.entity.DiagnosisDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnosis {

    private String id;

    private Patient patient;

    private Staff staff;

    private String diagnosis;

    private LocalDateTime dateTime;

    public static Diagnosis convert(DiagnosisDB diangosisDB){
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setId(diangosisDB.getId());
        diagnosis.setDiagnosis(diangosisDB.getDiagnosis());
        diagnosis.setPatient(Patient.convert(diangosisDB.getPatient()));
        diagnosis.setStaff(Staff.convert(diangosisDB.getStaff()));
        diagnosis.setDateTime(diangosisDB.getDateTime());
        return diagnosis;
    }

}
