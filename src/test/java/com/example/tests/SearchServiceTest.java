package com.example.tests;

import com.example.core.entity.Patient;
import com.example.core.service.SearchService;
import com.example.persistance.entity.PatientDB;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private Mutiny.SessionFactory sessionFactory;

    @Mock
    private Mutiny.Session session;

    @Mock
    private Mutiny.Query<PatientDB> mockQuery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.openSession()).thenReturn(Uni.createFrom().item(session));

        when(session.createQuery(eq("FROM PatientDB where name like ?1"), any(Class.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(eq(1), anyString())).thenReturn(mockQuery);

    }

    @Test
    public void testSearchPatientByName() {
        // Arrange
        String term = "John";
        PatientDB mockPatient = new PatientDB("1", "John Doe", 30, "M");
        List<PatientDB> patientList = Collections.singletonList(mockPatient);
        when(mockQuery.getResultList()).thenReturn(Uni.createFrom().item(patientList));

        // Act
        Uni<List<Patient>> resultUni = searchService.searchPatientByName(term);

        // Assert
        resultUni.subscribe().with(item -> {
            assertEquals(1, item.size());
            Patient result = item.get(0);
            assertEquals("John Doe", result.getName());
            // Add more assertions as needed
        });

        // Verify that the correct methods were called on the mocks
        verify(session).createQuery("FROM PatientDB where name like ?1", PatientDB.class);
        verify(mockQuery).setParameter(1, '%' + term + '%');
        verify(mockQuery).getResultList();
    }
    @Test
    public void testSearchPatientWithEmptyResult() {
        // Arrange
        String term = "NonExistent";
        when(mockQuery.getResultList()).thenReturn(Uni.createFrom().item(Collections.emptyList()));

        // Act
        Uni<List<Patient>> resultUni = searchService.searchPatientByName(term);

        // Assert
        resultUni.subscribe().with(item -> {
            assertTrue(item.isEmpty());
        });

        // Verify interactions
        verify(session).createQuery(any(String.class), eq(PatientDB.class));
        verify(mockQuery).setParameter(eq(1), anyString());
    }

    @Test
    public void testSearchPatientWithNullInput() {
        // Arrange
        String term = null;
        // Assume the other search methods are mocked similarly
        when(mockQuery.getResultList()).thenReturn(Uni.createFrom().item(Collections.emptyList()));

        // Act
        Uni<List<Patient>> resultUni = searchService.searchPatient(term);

        // Assert
        resultUni.subscribe().with(item -> {
            assertNotNull(item); // Ensure the result is not null
            assertTrue(item.isEmpty()); // Ensure the result is an empty list
        });
    }

    @Test
    public void testSearchPatientWithMultipleResults() {
        // Arrange
        String term = "John";
        PatientDB mockPatient1 = new PatientDB("1", "John Doe", 30, "M");
        PatientDB mockPatient2 = new PatientDB("2", "Johnny Appleseed", 25, "M");
        List<PatientDB> patientList = Arrays.asList(mockPatient1, mockPatient2);
        when(mockQuery.getResultList()).thenReturn(Uni.createFrom().item(patientList));

        // Act
        Uni<List<Patient>> resultUni = searchService.searchPatientByName(term);

        // Assert
        resultUni.subscribe().with(item -> {
            assertEquals(2, item.size());
        });
    }

    @Test
    public void testSearchPatientWithException() {
        // Arrange
        String term = "John";
        when(mockQuery.getResultList()).thenThrow(new RuntimeException("Database Error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            searchService.searchPatientByName(term).await().indefinitely();
        });
    }

    @Test
    public void testSearchPatientWithSpecialCharacters() {
        // Arrange
        String term = "Jo@hn!";
        PatientDB mockPatient = new PatientDB("1", "Jo@hn! Doe", 30, "M");
        List<PatientDB> patientList = Collections.singletonList(mockPatient);
        when(mockQuery.getResultList()).thenReturn(Uni.createFrom().item(patientList));

        // Act
        Uni<List<Patient>> resultUni = searchService.searchPatientByName(term);

        // Assert
        resultUni.subscribe().with(item -> {
            assertEquals(1, item.size());
            assertEquals("Jo@hn! Doe", item.get(0).getName());
        });
    }
}

