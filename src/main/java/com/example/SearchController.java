package com.example;

import com.example.core.entity.Encounter;
import com.example.core.entity.Patient;
import com.example.core.service.ISearchService;
import io.smallrye.mutiny.Uni;
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

    @Path("/patient")
    @GET()
    public Uni<List<Patient>> searchPatient(@QueryParam("value") String term) {
        System.out.println("controller");
        return searchService.searchPatient(term);
    }

    @Path("/encounter")
    @GET()
    public Uni<List<Encounter>> searchEncounter(@QueryParam("value") String term) {
        System.out.println("controller");
        return null;
    }
}