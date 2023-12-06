package com.example.view.entity;

import com.example.core.entity.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisSimpleView {
    private StaffView staff;
    private String diagnosis;
    private LocalDateTime dateTime;

    public static List<DiagnosisSimpleView> convert(List<Diagnosis> diagnoses){
        if(diagnoses == null) {
            return new ArrayList<>();
        }
        ArrayList<DiagnosisSimpleView> diagnosisSimpleViews = new ArrayList<>();
        for(Diagnosis d : diagnoses){
            diagnosisSimpleViews.add(DiagnosisSimpleView.convert(d));
        }
        return diagnosisSimpleViews;
    }
    public static DiagnosisSimpleView convert(Diagnosis diagnosis){
        DiagnosisSimpleView dView = new DiagnosisSimpleView();
        dView.setDiagnosis(diagnosis.getDiagnosis());
        dView.setStaff(StaffView.convert(diagnosis.getStaff()));
        dView.setDateTime(diagnosis.getDateTime());

        return dView;
    }

}
