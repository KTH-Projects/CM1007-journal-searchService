package com.example.core.entity;

import com.example.persistance.entity.ObservationDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Observation {

    private String id;
    private Encounter encounter;
    private String observation;

    public static Observation convert(ObservationDB oDB){
        Observation o = new Observation();

        o.setObservation(oDB.getObservation());
        o.setId(oDB.getId());
        o.setEncounter(Encounter.convert(oDB.getEncounter()));

        return o;
    }

}
