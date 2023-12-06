package com.example.core.service;

import com.example.persistance.entity.DiagnosisDB;
import com.example.persistance.entity.EncounterDB;
import com.example.persistance.entity.PatientDB;
import com.example.persistance.entity.StaffDB;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class SearchService implements ISearchService{

    @Override
    @WithSession
    public Uni<List<Object>> search(String term) {
        if (term == null || term.trim().isEmpty()) {
            return Uni.createFrom().item(new ArrayList<>());
        }

        Uni<List<Object>> patientSearch = PatientDB.find("name like ?1 or cast(age as string) like ?1 or sex like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(patients -> Uni.createFrom().item(new ArrayList<>(patients)));

        Uni<List<Object>> staffSearch = StaffDB.find("name like ?1 or cast(age as string) like ?1 or sex like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(staff -> Uni.createFrom().item(new ArrayList<>(staff)));

        Uni<List<Object>> encounterSearch = EncounterDB.find("patient.name like ?1 or staff.name like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(encounters -> Uni.createFrom().item(new ArrayList<>(encounters)));

        Uni<List<Object>> diagnosisSearch = DiagnosisDB.find("diagnosis like ?1 or patient.name like ?1 or staff.name like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(diagnoses -> Uni.createFrom().item(new ArrayList<>(diagnoses)));

        return patientSearch.flatMap(patients -> staffSearch
                .flatMap(staff -> {
                            patients.addAll(staff);
                            return encounterSearch;
                        })
                        .flatMap(encounters -> {
                            patients.addAll(encounters);
                            return diagnosisSearch;
                        })
                        .onItem().transform(diagnoses -> {
                            patients.addAll(diagnoses);
                            return patients;
                        })
        );

    }

}
