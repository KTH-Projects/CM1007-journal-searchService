package com.example.core.entity;


import com.example.persistance.entity.PatientDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String id;
    private String name;
    private int age;
    private String sex;
    private List<Encounter> encounters;
    private List<Diagnosis> diagnoses;

    public static Patient convert(PatientDB pDB){
        System.out.println(pDB);
        Patient p = new Patient();
        p.setId(pDB.getId());
        p.setAge(pDB.getAge());
        p.setSex(pDB.getSex());
        p.setName(pDB.getName());
        return p;
    }

}
