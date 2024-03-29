package com.example;

import com.example.core.entity.Encounter;
import com.example.core.entity.Patient;
import com.example.core.service.ISearchService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("/search")
public class SearchController {

    @Inject
    ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
        System.out.println(System.getenv());
    }

    @RolesAllowed({"doctor", "staff"})
    @Path("/patient")
    @GET()
    public Uni<List<Patient>> searchPatient(@QueryParam("value") String term) {
        return searchService.searchPatient(term);
    }

    @RolesAllowed({"doctor", "staff"})
    @Path("/encounter")
    @GET()
    public Uni<List<Encounter>> searchEncounter(@QueryParam("value") String term) {
        return searchService.searchEncounter(term);
    }
}