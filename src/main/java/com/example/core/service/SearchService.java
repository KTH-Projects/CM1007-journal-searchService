package com.example.core.service;

import com.example.core.entity.Encounter;
import com.example.core.entity.Patient;
import com.example.persistance.entity.DiagnosisDB;
import com.example.persistance.entity.EncounterDB;
import com.example.persistance.entity.PatientDB;
import com.example.persistance.entity.StaffDB;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@ApplicationScoped
public class SearchService implements ISearchService {
    @Inject
    Mutiny.SessionFactory sessionFactory;


    @Override
    public Uni<List<Patient>> searchPatient(String term) {
        if (term == null || term.trim().isEmpty()) {
            return Uni.createFrom().item(new ArrayList<>());
        }

        Uni<List<Patient>> patients = searchPatientByName(term);
        Uni<List<Patient>> diagnosisPatientSearch = searchPatientByDiagnosis(term);
        Uni<List<Patient>> staffPatientSearch = searchPatientByStaff(term);

        //return staffPatientSearch;

        // Merge the two Uni lists into one
        return mergePatientLists(patients,mergePatientLists(diagnosisPatientSearch, staffPatientSearch));
    }

    @Override
    public Uni<List<Encounter>> searchEncounter(String term) {
        return null;
    }

    public Uni<List<Patient>> searchPatientByName(String term) {
        return sessionFactory.openSession()
                .flatMap(session ->
                        session.createQuery("FROM PatientDB where name like ?1", PatientDB.class)
                                .setParameter(1,'%' + term + '%')
                                .getResultList()
                                .onItem().transformToUni(patientDB -> {
                                    List<Patient> patients = patientDB.isEmpty() ? Collections.emptyList() : patientDB.stream().map(Patient::convert).collect(Collectors.toList());
                                    return Uni.createFrom().item(patients);
                                })
                                .eventually(session::close)
                );
    }


    private Uni<List<Patient>> searchPatientByDiagnosis(String term) {
        return sessionFactory.openSession() // Open a new session
                .flatMap(session ->
                        session.createQuery("from DiagnosisDB where diagnosis like ?1", DiagnosisDB.class)
                                .setParameter(1, '%' + term + '%')
                                .getResultList()
                                .onItem().transformToUni(diagnosisDBs -> {
                                    List<Patient> patients = diagnosisDBs.isEmpty() ?
                                            Collections.emptyList() :
                                            diagnosisDBs.stream()
                                                    .map(DiagnosisDB::getPatient)
                                                    .distinct()
                                                    .map(Patient::convert)
                                                    .collect(Collectors.toList());
                                    return Uni.createFrom().item(patients);
                                })
                                .eventually(session::close) // Correct usage of session closure
                );
    }



    private Uni<List<Patient>> searchPatientByStaff(String term){
        return findStaffIds(term)
                .onItem().transformToUni(this::findEncountersByStaffIds)
                .onItem().transformToUni(this::extractPatientsFromEncounters);
    }

    private Uni<List<String>> findStaffIds(String term) {
        return sessionFactory.openSession()
                .flatMap(session ->
                        session.createQuery("FROM StaffDB WHERE name like ?1", StaffDB.class) // Use a named query or adjust as needed
                                .setParameter(1,'%' + term + '%')
                                .getResultList()
                                .onItem().transform(staffDBs -> staffDBs.stream()
                                        .map(StaffDB::getId)
                                        .collect(Collectors.toList()))
                                .eventually(session::close)
                );
    }
    private Uni<List<EncounterDB>> findEncountersByStaffIds(List<String> staffIds) {
        if (staffIds.isEmpty()) {
            return Uni.createFrom().item(Collections.emptyList());
        }

        return sessionFactory.openSession()
                .flatMap(session -> {
                    // Perform encounter searches using the session
                    List<Uni<List<EncounterDB>>> encounterSearches = staffIds.stream()
                            .map(staffId -> session.createQuery("FROM EncounterDB WHERE staff.id like ?1", EncounterDB.class) // Use a named query or adjust as needed
                                    .setParameter(1, staffId)
                                    .getResultList())
                            .collect(Collectors.toList());

                    return Uni.combine().all().unis(encounterSearches)
                            .combinedWith(results -> results.stream()
                                    .flatMap(list -> ((List<EncounterDB>) list).stream())
                                    .collect(Collectors.toList()))
                            .eventually(session::close);
                });
    }

    private Uni<List<Patient>> extractPatientsFromEncounters(List<EncounterDB> encounters) {
        if (encounters.isEmpty()) {
            // If there are no encounters found, return an empty list wrapped in a Uni directly.
            return Uni.createFrom().item(Collections.emptyList());
        }

        return Uni.createFrom().item(
                encounters.stream()
                        .map(EncounterDB::getPatient)
                        .distinct()
                        .map(Patient::convert)
                        .collect(Collectors.toList())
        );
    }

    private Uni<List<Patient>> mergePatientLists(Uni<List<Patient>> listOne, Uni<List<Patient>> listTwo) {
        // If either Uni is null, replace it with an empty Uni.
        Uni<List<Patient>> safeListOne = listOne != null ? listOne : Uni.createFrom().item(Collections.emptyList());
        Uni<List<Patient>> safeListTwo = listTwo != null ? listTwo : Uni.createFrom().item(Collections.emptyList());

        return Uni.combine().all().unis(safeListOne, safeListTwo)
                .combinedWith((patientsOne, patientsTwo) -> {
                    // Use a set to eliminate duplicates after merging
                    Set<Patient> patientSet = new HashSet<>(patientsOne);
                    patientSet.addAll(patientsTwo);
                    // Convert the set back to a list before returning
                    return new ArrayList<>(patientSet);
                });
    }

 /*
        System.out.println("after null check");
        Uni<List<Object>> patientSearch = PatientDB.find("name like ?1 or cast(age as string) like ?1 or sex like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(patients -> Uni.createFrom().item(new ArrayList<>(patients)));

        System.out.println("after patient");
        Uni<List<Object>> staffSearch = StaffDB.find("name like ?1 or cast(age as string) like ?1 or sex like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(staff -> Uni.createFrom().item(new ArrayList<>(staff)));
        System.out.println("after staff");
        Uni<List<Object>> encounterSearch = EncounterDB.find("patient.name like ?1 or staff.name like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(encounters -> Uni.createFrom().item(new ArrayList<>(encounters)));
        System.out.println("after encounter");
        Uni<List<Object>> diagnosisSearch = DiagnosisDB.find("diagnosis like ?1 or patient.name like ?1 or staff.name like ?1", '%' + term + '%')
                .list()
                .onItem().transformToUni(diagnoses -> Uni.createFrom().item(new ArrayList<>(diagnoses)));
        System.out.println("after diangosis");
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

         */

}
