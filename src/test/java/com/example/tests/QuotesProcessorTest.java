package com.example.tests;

import com.example.core.entity.Encounter;
import com.example.core.entity.Patient;
import com.example.core.service.ISearchService;
import com.example.kafka.processor.QuotesProcessor;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class QuotesProcessorTest {

    @InjectMocks
    private QuotesProcessor quotesProcessor;

    @Mock
    private ISearchService searchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcess() throws InterruptedException {
        // Arrange
        String term = "test-term";
        List<Patient> expectedPatients = List.of(new Patient("1", "Test Patient", 30, "M", null, null));
        when(searchService.searchPatient(term)).thenReturn(Uni.createFrom().item(expectedPatients));

        // Act
        Uni<List<Patient>> resultUni = quotesProcessor.process(term);

        // Assert
        resultUni.subscribe().with(item -> {
            assertNotNull(item); // Ensure the result is not null
            assertEquals(expectedPatients, item); // Ensure the result matches the expected output
        });
    }

    @Test
    public void testProcessWithPatientSearch() throws InterruptedException {
        // Arrange
        String term = "test-patient";
        List<Encounter> encounters = List.of(new Encounter());
        Patient expectedPatient = new Patient("1", "Test Patient", 30, "M", encounters, null);
        when(searchService.searchPatient(term)).thenReturn(Uni.createFrom().item(List.of(expectedPatient)));

        // Act
        Uni<List<Patient>> resultUni = quotesProcessor.process(term);

        // Assert
        resultUni.subscribe().with(patients -> {
            assertNotNull(patients); // Ensure the result is not null
            assertFalse(patients.isEmpty()); // Ensure the list isn't empty
            Patient patient = patients.get(0); // Get the first patient
            assertEquals("Test Patient", patient.getName()); // Validate patient details
            assertNotNull(patient.getEncounters()); // Ensure encounters are present
            assertEquals("encounterId1", patient.getEncounters().get(0).getId()); // Validate encounter details
        });
    }
}

