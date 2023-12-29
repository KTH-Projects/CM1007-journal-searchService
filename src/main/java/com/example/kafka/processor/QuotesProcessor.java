package com.example.kafka.processor;

import com.example.core.entity.Patient;
import com.example.core.service.ISearchService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.List;

@ApplicationScoped
public class QuotesProcessor {

    @Inject
    ISearchService searchService;

    @Incoming("quote-requests")
    @Outgoing("quotes")
    public Uni<List<Patient>> process(String quoteRequest) throws InterruptedException {
        System.out.println("Processing quote request: " + quoteRequest);
        return searchService.searchPatient(quoteRequest);
    }
}
