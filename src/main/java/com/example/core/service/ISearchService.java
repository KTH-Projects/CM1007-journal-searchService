package com.example.core.service;

import com.example.core.entity.Encounter;
import com.example.core.entity.Patient;
import io.smallrye.mutiny.Uni;

import java.util.List;

public interface ISearchService {
    public Uni<List<Patient>> searchPatient(String term);
    public Uni<List<Encounter>> searchEncounter(String term);
}
