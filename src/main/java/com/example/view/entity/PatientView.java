package com.example.view.entity;

import com.example.core.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientView {
    private String id;
    private String name;
    private int age;
    private String sex;
    private List<DiagnosisSimpleView> diagnoses;
    private List<EncounterView> encounters;

    public static PatientView convert(Patient p){
        PatientView pView = new PatientView();
        pView.setId(p.getId());
        pView.setName(p.getName());
        pView.setSex(p.getSex());
        pView.setAge(p.getAge());

        pView.setDiagnoses(DiagnosisSimpleView.convert(p.getDiagnoses()));
        pView.setEncounters(EncounterView.convert(p.getEncounters()));

        return pView;
    }
}
