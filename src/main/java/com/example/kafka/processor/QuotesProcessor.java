package com.example.kafka.processor;

import com.example.core.entity.Patient;
import com.example.core.service.ISearchService;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.kafka.model.*;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.util.List;
import java.util.Random;

/**
 * A bean consuming data from the "quote-requests" Kafka topic (mapped to "requests" channel) and giving out a random quote.
 * The result is pushed to the "quotes" Kafka topic.
 */
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
