package com.neosbk.wastesorting.service;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.exception.ResourceNotFoundException;
import com.neosbk.wastesorting.mapper.DisposalGuidelineMapper;
import com.neosbk.wastesorting.model.DisposalGuideline;
import com.neosbk.wastesorting.repository.DisposalGuidelineRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DisposalGuidelineTest {


    @Mock
    private DisposalGuidelineRepository repository;

    @Mock
    private DisposalGuidelineMapper mapper;

    @InjectMocks
    private DisposalGuidelineService service;

    private DisposalGuideline guideline;
    private DisposalGuideline guideline2;
    private DisposalGuidelineDTO guidelineDTO;
    private DisposalGuidelineDTO guidelineDTO2;

    @BeforeEach
    void setUp(){
        guideline = new DisposalGuideline(1L,"Glass Jar","Rinse and place in the recycling bin.");
        guideline2 = new DisposalGuideline(2L,"Newspaper","Place in the recycling bin.");
        guidelineDTO = new DisposalGuidelineDTO(guideline.getItemName(),guideline.getDisposalGuideline());
        guidelineDTO2 = new DisposalGuidelineDTO(guideline2.getItemName(),guideline2.getDisposalGuideline());
    }

    @Test
    void getAllDisposalGuidelines_shouldReturnListOfGuidelineDTOs_whenGuidelinesExist() {
        when(repository.findAll()).thenReturn(List.of(guideline, guideline2));
        when(mapper.toDTO(guideline)).thenReturn(guidelineDTO);
        when(mapper.toDTO(guideline2)).thenReturn(guidelineDTO2);

        List<DisposalGuidelineDTO> result = service.getDisposalGuidelines();

        assertThat(result).hasSize(2)
                .containsExactly(guidelineDTO, guidelineDTO2)
                .extracting(DisposalGuidelineDTO :: itemName, DisposalGuidelineDTO:: disposalGuideline)
                .containsExactly(
                        tuple("Glass Jar","Rinse and place in the recycling bin."),
                        tuple("Newspaper","Place in the recycling bin.")
                );

        verify(repository).findAll();
        verify(mapper).toDTO(guideline);
        verify(mapper).toDTO(guideline2);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getAllDisposalGuidelines_shouldReturnEmptyList_whenNoGuidelinesExist(){
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThat(service.getDisposalGuidelines()).isEmpty();

        verify(repository).findAll();
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void getGuidelineById_shouldReturnGuidelineDTO_whenGuidelineExists() {
        when(repository.findById(guideline.getId())).thenReturn(Optional.of(guideline));
        when(mapper.toDTO(guideline)).thenReturn(guidelineDTO);

        DisposalGuidelineDTO result =  service.getGuidelineById(guideline.getId());

        assertThat(result).isEqualTo(guidelineDTO)
                .extracting(
                        DisposalGuidelineDTO :: itemName,
                        DisposalGuidelineDTO :: disposalGuideline)
                .containsExactly("Glass Jar","Rinse and place in the recycling bin.");

        verify(repository).findById(guideline.getId());
        verify(mapper).toDTO(guideline);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getGuidelineById_shouldThrowResourceNotFoundException_whenGuidelineNotFound() {
        Long invalidId = 105L;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getGuidelineById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Disposal Guideline Not Found For ID: " + invalidId);

        verify(repository).findById(invalidId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createDisposalGuideline_shouldSaveAndReturnGuidelineDTO_whenValidRequest() {
        DisposalGuidelineDTO request = guidelineDTO;
        DisposalGuideline unsaved = new DisposalGuideline(null,"Glass Jar","Rinse and place in the recycling bin.");
        DisposalGuidelineDTO response = guidelineDTO;

        when(mapper.toEntity(request)).thenReturn(unsaved);
        when(repository.save(unsaved)).thenReturn(guideline);
        when(mapper.toDTO(guideline)).thenReturn(response);

        DisposalGuidelineDTO result = service.createDisposalGuideline(request);

        assertThat(result).isEqualTo(response);

        verify(mapper).toEntity(request);
        verify(repository).save(argThat(e ->
                e.getItemName().equals("Glass Jar") && e.getDisposalGuideline().equals("Rinse and place in the recycling bin.")
        ));
        verify(mapper).toDTO(guideline);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void createDisposalGuideline_shouldPropagateDataAccessException_whenRepositorySaveFails() {
        DisposalGuidelineDTO request = guidelineDTO;
        when(mapper.toEntity(request)).thenReturn(new DisposalGuideline());
        when(repository.save(any())).thenThrow(new DataAccessException("Database error") {});

        assertThatThrownBy(() -> service.createDisposalGuideline(request))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");

        verify(mapper, never()).toDTO(any());
    }

    @Test
    void updateGuideline_shouldUpdateAndReturnUpdatedGuidelineDTO_whenValidRequest() {
        DisposalGuidelineDTO updatedGuidelineDTO = new DisposalGuidelineDTO("Updated Name", "Updated Guideline");
        when(repository.findById(guideline.getId())).thenReturn(Optional.of(guideline));
        when(repository.save(any(DisposalGuideline.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(any(DisposalGuideline.class))).thenReturn(updatedGuidelineDTO);

        DisposalGuidelineDTO result = service.updateDisposalGuideline(guideline.getId(), updatedGuidelineDTO);

        assertThat(result).isEqualTo(updatedGuidelineDTO);

        verify(repository).findById(guideline.getId());
        verify(repository).save(argThat(guideline ->
                guideline.getItemName().equals("Updated Name") &&
                        guideline.getDisposalGuideline().equals("Updated Guideline")
        ));
        verify(mapper).toDTO(any(DisposalGuideline.class));
        verify(repository).findById(guideline.getId());
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deleteGuidelineById_shouldDeleteGuideline_whenGuidelineExists() {
        Long guidelineId = guideline.getId();
        when(repository.existsById(guidelineId)).thenReturn(true);

        service.deleteDisposalGuideline(guidelineId);

        verify(repository).deleteById(guidelineId);
    }

    @Test
    void deleteGuidelineById_shouldThrowResourceNotFoundException_whenGuidelineNotFound() {
        Long nonExistingId = 99L;
        when(repository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteDisposalGuideline(nonExistingId));
        verify(repository, never()).deleteById(any());
    }

}
