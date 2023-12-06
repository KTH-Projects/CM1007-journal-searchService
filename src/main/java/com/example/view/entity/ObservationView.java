package com.example.view.entity;

import com.example.core.entity.Observation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObservationView {

    private String id;
    private String observation;


    public static List<ObservationView> convert(List<Observation> observations){
        if(observations == null) {
            return new ArrayList<>();
        }
        ArrayList<ObservationView> observationViews = new ArrayList<>();
        for(Observation d : observations){
            observationViews.add(ObservationView.convert(d));
        }
        return observationViews;
    }
    public static ObservationView convert(Observation observation){
        ObservationView oView = new ObservationView();
        oView.setId(observation.getId());
        oView.setObservation(observation.getObservation());
        return oView;
    }

}
