package com.example.view.entity;

import com.example.core.entity.Diagnosis;
import com.example.core.entity.Patient;
import com.example.core.entity.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisView {
    private Patient patient;
    private Staff staff;
    private String diagnosis;
    private LocalDateTime dateTime;

    public static DiagnosisView convert(Diagnosis diagnosis){
        DiagnosisView dView = new DiagnosisView();
        dView.setDiagnosis(diagnosis.getDiagnosis());
        dView.setPatient(diagnosis.getPatient());
        dView.setStaff(diagnosis.getStaff());
        dView.setDateTime(diagnosis.getDateTime());

        return dView;
    }

}
