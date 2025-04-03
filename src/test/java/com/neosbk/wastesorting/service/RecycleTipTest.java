package com.neosbk.wastesorting.service;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.exception.ResourceNotFoundException;
import com.neosbk.wastesorting.mapper.RecycleTipMapper;
import com.neosbk.wastesorting.model.RecycleTip;
import com.neosbk.wastesorting.repository.RecycleTipRepository;
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
public class RecycleTipTest {

    @Mock
    private RecycleTipRepository repository;

    @Mock
    private RecycleTipMapper mapper;

    @InjectMocks
    private RecycleTipService service;

    private RecycleTip Tip;
    private RecycleTip Tip2;
    private RecycleTipDTO TipDTO;
    private RecycleTipDTO TipDTO2;

    @BeforeEach
    void setUp() {
        Tip = new RecycleTip(1L,"Plastic Bottle","Remove the cap before recycling.");
        Tip2 = new RecycleTip(2L,"Glass Jar","Check if the glass is recyclable in your area.");
        TipDTO = new RecycleTipDTO(Tip.getItemName(),Tip.getRecyclingTip());
        TipDTO2 = new RecycleTipDTO(Tip2.getItemName(),Tip2.getRecyclingTip());
    }

    @Test
    void getAllRecycleTips_shouldReturnListOfTipDTOs_whenTipsExist() {
        when(repository.findAll()).thenReturn(List.of(Tip, Tip2));
        when(mapper.toDTO(Tip)).thenReturn(TipDTO);
        when(mapper.toDTO(Tip2)).thenReturn(TipDTO2);

        List<RecycleTipDTO> result = service.getRecycleTips();

        assertThat(result)
                .hasSize(2)
                .containsExactly(TipDTO, TipDTO2)
                .extracting(
                        RecycleTipDTO::itemName,
                        RecycleTipDTO::recyclingTip
                )
                .containsExactly(
                        tuple("Plastic Bottle", "Remove the cap before recycling."),
                        tuple("Glass Jar", "Check if the glass is recyclable in your area.")
                );

        verify(repository).findAll();
        verify(mapper).toDTO(Tip);
        verify(mapper).toDTO(Tip2);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getAllRecycleTips_shouldReturnEmptyList_whenNoTipsExist() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThat(service.getRecycleTips()).isEmpty();

        verify(repository).findAll();
        verifyNoInteractions(mapper);
    }

    @Test
    void getRecycleTipById_shouldReturnTipDTO_whenTipExists() {
        when(repository.findById(Tip.getId())).thenReturn(Optional.of(Tip));
        when(mapper.toDTO(Tip)).thenReturn(TipDTO);

        RecycleTipDTO result = service.getRecycleTipById(Tip.getId());

        assertThat(result)
                .isEqualTo(TipDTO)
                .extracting(
                        RecycleTipDTO::itemName,
                        RecycleTipDTO::recyclingTip
                )
                .containsExactly("Plastic Bottle", "Remove the cap before recycling.");

        verify(repository).findById(Tip.getId());
        verify(mapper).toDTO(Tip);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getRecycleTipById_shouldThrowResourceNotFoundException_whenTipNotFound() {
        Long invalidId = 101L;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRecycleTipById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Recycle Tip Not Found For ID: " + invalidId);

        verify(repository).findById(invalidId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createRecycleTip_shouldSaveAndReturnTipDTO_whenValidRequest() {
        RecycleTipDTO request = TipDTO;
        RecycleTip unsaved = new RecycleTip(null, TipDTO.itemName(), TipDTO.recyclingTip());
        RecycleTipDTO response = TipDTO;

        when(mapper.toEntity(request)).thenReturn(unsaved);
        when(repository.save(unsaved)).thenReturn(Tip);
        when(mapper.toDTO(Tip)).thenReturn(response);

        RecycleTipDTO result = service.createRecycleTip(request);

        assertThat(result).isEqualTo(response);

        verify(mapper).toEntity(request);
        verify(repository).save(argThat(e ->
                e.getItemName().equals("Plastic Bottle") && e.getRecyclingTip().equals("Remove the cap before recycling.")
        ));
        verify(mapper).toDTO(Tip);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void createRecycleTip_shouldPropagateDataAccessException_whenRepositorySaveFails() {
        RecycleTipDTO request = TipDTO;
        when(mapper.toEntity(request)).thenReturn(new RecycleTip());
        when(repository.save(any())).thenThrow(new DataAccessException("Database error") {});

        assertThatThrownBy(() -> service.createRecycleTip(request))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");

        verify(mapper, never()).toDTO(any());
    }

    @Test
    void updateRecycleTip_shouldUpdateAndReturnUpdatedTipDTO_whenValidRequest() {
        RecycleTipDTO updatedTipDTO = new RecycleTipDTO("Updated Name", "Updated Tip");
        when(repository.findById(Tip.getId())).thenReturn(Optional.of(Tip));
        when(repository.save(any(RecycleTip.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(any(RecycleTip.class))).thenReturn(updatedTipDTO);

        RecycleTipDTO result = service.updateRecycleTip(Tip.getId(), updatedTipDTO);

        assertThat(result).isEqualTo(updatedTipDTO);

        verify(repository).findById(Tip.getId());
        verify(repository).save(argThat(tip ->
                tip.getItemName().equals("Updated Name") &&
                        tip.getRecyclingTip().equals("Updated Tip")
        ));
        verify(mapper).toDTO(any(RecycleTip.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteRecycleTipById_shouldDeleteTip_whenTipExists() {
        Long tipId = Tip.getId();
        when(repository.existsById(tipId)).thenReturn(true);

        service.deleteRecycleTip(tipId);

        verify(repository).deleteById(tipId);
    }

    @Test
    void deleteRecycleTipById_shouldThrowResourceNotFoundException_whenTipNotFound() {
        Long nonExistingId = 99L;
        when(repository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteRecycleTip(nonExistingId));
        verify(repository, never()).deleteById(any());
    }
}
