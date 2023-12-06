package com.example;

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


    @GET
    public Uni<List<Object>> search(@QueryParam("value") String term) {
        return searchService.search(term);
    }
}