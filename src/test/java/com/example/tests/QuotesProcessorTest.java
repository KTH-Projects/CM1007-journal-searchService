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
            assertNotNull(item);
            assertEquals(expectedPatients, item);
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
            assertNotNull(patients);
            assertFalse(patients.isEmpty());
            Patient patient = patients.get(0);
            assertEquals("Test Patient", patient.getName());
            assertNotNull(patient.getEncounters());
            assertEquals("encounterId1", patient.getEncounters().get(0).getId());
        });
    }
}

