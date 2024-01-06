package com.example.tests;


import com.example.core.entity.Diagnosis;
import com.example.core.entity.Encounter;
import com.example.core.entity.Patient;
import com.example.kafka.producer.QuotesResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class QuotesResourceTest {

    @InjectMocks
    private QuotesResource quotesResource;

    @Mock
    private Emitter<String> quoteRequestEmitter;

    @Mock
    private Multi<Uni<List<Patient>>> quotes;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRequest() {
        // Arrange
        String term = "test-term";

        // Act
        String response = quotesResource.createRequest(term);

        // Assert
        verify(quoteRequestEmitter).send(term); // Verify that the term was sent to the Kafka topic
        assertEquals(term, response); // Optional: Verify the response is as expected
    }
    @Test
    public void testStream() {
        // Arrange
        List<Encounter> encounters = List.of(new Encounter());
        List<Diagnosis> diagnoses = List.of(new Diagnosis());
        Patient testPatient = new Patient("1", "Test Patient", 30, "M", encounters, diagnoses);

        Uni<List<Patient>> uniMock = Uni.createFrom().item(List.of(testPatient));
        Multi<Uni<List<Patient>>> multiMock = Multi.createFrom().item(uniMock);
        when(quotes.log()).thenReturn(multiMock);

        // Act
        Multi<Uni<List<Patient>>> result = quotesResource.stream();

        // Assert
        result.subscribe().with(item -> {
            // Subscribe to the Uni within the Multi and verify the emitted items
            item.subscribe().with(patients -> {
                assertFalse(patients.isEmpty()); // Check the list isn't empty
                Patient patient = patients.get(0); // Get the first patient
                assertEquals("1", patient.getId()); // Validate the patient details
            });
        });
    }

}

