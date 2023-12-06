package com.example.core.entity;


import com.example.persistance.entity.EncounterDB;
import com.example.persistance.entity.ObservationDB;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encounter {

    private String id;

    private Patient patient;

    private Staff staff;

    private LocalDateTime dateTime;

    private List<Observation> observations;

    public static Encounter convert(EncounterDB eDB){
        Encounter e = new Encounter();

        e.setId(eDB.getId());
        e.setStaff(Staff.convert(eDB.getStaff()));
        e.setPatient(Patient.convert(eDB.getPatient()));
        e.setDateTime(eDB.getDateTime());
        if(eDB.getObservations() != null){
            ArrayList<Observation> observations = new ArrayList<>();
            for(ObservationDB oDB : eDB.getObservations()){
                Observation observation = new Observation();
                observation.setObservation(oDB.getObservation());
                observation.setId(oDB.getId());
                observation.setEncounter(e);

                observations.add(observation);
            }
            e.setObservations(observations);

        }

        return e;
    }

}
