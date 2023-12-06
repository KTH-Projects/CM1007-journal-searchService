package com.example.view.entity;

import com.example.core.entity.Encounter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncounterView {

    private String id;
    private String patientName;
    private String staffName;
    private List<ObservationView> observations;
    private LocalDateTime dateTime;

    public static List<EncounterView> convert(List<Encounter> encounters){
        if(encounters == null) {
            return new ArrayList<>();
        }

        ArrayList<EncounterView> encounterViews = new ArrayList<>();
        for(Encounter d : encounters){
            encounterViews.add(EncounterView.convert(d));
        }
        return encounterViews;
    }
    public static EncounterView convert(Encounter encounter){
        EncounterView eView = new EncounterView();
        eView.setId(encounter.getId());
        eView.setDateTime(encounter.getDateTime());
        eView.setStaffName(encounter.getStaff().getName());
        eView.setPatientName(encounter.getPatient().getName());
        eView.setObservations(ObservationView.convert(encounter.getObservations()));
        return eView;
    }


}
