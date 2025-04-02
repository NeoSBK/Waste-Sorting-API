package com.neosbk.wastesorting.service;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.exception.ResourceNotFoundException;
import com.neosbk.wastesorting.mapper.WasteCategoryMapper;
import com.neosbk.wastesorting.model.WasteCategory;
import com.neosbk.wastesorting.repository.WasteCategoryRepository;
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
class WasteCategoryServiceTest {

    @Mock
    private WasteCategoryRepository repository;

    @Mock
    private WasteCategoryMapper mapper;

    @InjectMocks
    private WasteCategoryService service;

    private WasteCategory plastic;
    private WasteCategory glass;
    private WasteCategoryDTO plasticDTO;
    private WasteCategoryDTO glassDTO;

    @BeforeEach
    void setUp() {
        plastic = new WasteCategory(1L, "Plastic", "Recyclable plastic materials");
        glass = new WasteCategory(2L, "Glass", "Recyclable glass materials");
        plasticDTO = new WasteCategoryDTO(plastic.getCategoryName(), plastic.getCategoryDescription());
        glassDTO = new WasteCategoryDTO(glass.getCategoryName(), glass.getCategoryDescription());
    }

    @Test
    void getAllCategories_shouldReturnListOfCategoryDTOs_whenCategoriesExist() {
        when(repository.findAll()).thenReturn(List.of(plastic, glass));
        when(mapper.toDTO(plastic)).thenReturn(plasticDTO);
        when(mapper.toDTO(glass)).thenReturn(glassDTO);

        List<WasteCategoryDTO> result = service.getCategories();

        assertThat(result)
                .hasSize(2)
                .containsExactly(plasticDTO, glassDTO)
                .extracting(
                        WasteCategoryDTO::categoryName,
                        WasteCategoryDTO::categoryDescription
                )
                .containsExactly(
                        tuple("Plastic", "Recyclable plastic materials"),
                        tuple("Glass", "Recyclable glass materials")
                );

        verify(repository).findAll();
        verify(mapper).toDTO(plastic);
        verify(mapper).toDTO(glass);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getAllCategories_shouldReturnEmptyList_whenNoCategoriesExist() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        assertThat(service.getCategories()).isEmpty();

        verify(repository).findAll();
        verifyNoInteractions(mapper);
    }

    @Test
    void getCategoryById_shouldReturnMappedDTO() {
        when(repository.findById(plastic.getId())).thenReturn(Optional.of(plastic));
        when(mapper.toDTO(plastic)).thenReturn(plasticDTO);

        WasteCategoryDTO result = service.getCategoryById(plastic.getId());

        assertThat(result)
                .isEqualTo(plasticDTO) // Primary check
                .extracting(
                        WasteCategoryDTO::categoryName,
                        WasteCategoryDTO::categoryDescription
                )
                .containsExactly("Plastic", "Recyclable plastic materials");

        verify(repository).findById(plastic.getId());
        verify(mapper).toDTO(plastic);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getCategoryById_shouldReturnCategoryDTO_whenCategoryExists() {
        when(repository.findById(plastic.getId())).thenReturn(Optional.of(plastic));
        when(mapper.toDTO(plastic)).thenReturn(plasticDTO);

        WasteCategoryDTO result = service.getCategoryById(plastic.getId());

        assertThat(result)
                .isEqualTo(plasticDTO)
                .extracting(
                        WasteCategoryDTO::categoryName,
                        WasteCategoryDTO::categoryDescription
                )
                .containsExactly("Plastic", "Recyclable plastic materials");

        verify(repository).findById(plastic.getId());
        verify(mapper).toDTO(plastic);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void getCategoryById_shouldThrowResourceNotFoundException_whenCategoryNotFound() {
        Long invalidId = 101L;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCategoryById(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category Not Found For ID: " + invalidId);

        verify(repository).findById(invalidId);
        verifyNoInteractions(mapper);
    }

    @Test
    void createCategory_shouldSaveAndReturnCategoryDTO_whenValidRequest() {
        WasteCategoryDTO request = plasticDTO;
        WasteCategory unsaved = new WasteCategory(null, plasticDTO.categoryName(), plasticDTO.categoryDescription());
        WasteCategoryDTO response = plasticDTO;

        when(mapper.toEntity(request)).thenReturn(unsaved);
        when(repository.save(unsaved)).thenReturn(plastic);
        when(mapper.toDTO(plastic)).thenReturn(response);

        WasteCategoryDTO result = service.createCategory(request);

        assertThat(result).isEqualTo(response);

        verify(mapper).toEntity(request);
        verify(repository).save(argThat(e ->
                e.getCategoryName().equals("Plastic") && e.getCategoryDescription().equals("Recyclable plastic materials")
        ));
        verify(mapper).toDTO(plastic);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void createCategory_shouldPropagateDataAccessException_whenRepositorySaveFails() {
        WasteCategoryDTO request = plasticDTO;
        when(mapper.toEntity(request)).thenReturn(new WasteCategory());
        when(repository.save(any())).thenThrow(new DataAccessException("Database error") {});

        assertThatThrownBy(() -> service.createCategory(request))
                .isInstanceOf(DataAccessException.class)
                .hasMessageContaining("Database error");

        verify(mapper, never()).toDTO(any());
    }

    @Test
    void updateCategory_shouldUpdateAndReturnUpdatedCategoryDTO_whenValidRequest() {
        WasteCategoryDTO updatedCategoryDTO = new WasteCategoryDTO("Updated Name", "Updated Description");
        when(repository.findById(plastic.getId())).thenReturn(Optional.of(plastic));
        when(repository.save(any(WasteCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(any(WasteCategory.class))).thenReturn(updatedCategoryDTO);

        WasteCategoryDTO result = service.updateCategory(plastic.getId(), updatedCategoryDTO);

        assertThat(result).isEqualTo(updatedCategoryDTO);

        verify(repository).findById(plastic.getId());
        verify(repository).save(argThat(category ->
                category.getCategoryName().equals("Updated Name") &&
                        category.getCategoryDescription().equals("Updated Description")
        ));
        verify(mapper).toDTO(any(WasteCategory.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteCategoryById_shouldDeleteCategory_whenCategoryExists() {
        Long categoryId = plastic.getId();
        when(repository.existsById(categoryId)).thenReturn(true);

        service.deleteCategory(categoryId);

        verify(repository).deleteById(categoryId);
    }

    @Test
    void deleteCategoryById_shouldThrowResourceNotFoundException_whenCategoryNotFound() {
        Long nonExistingId = 129L;
        when(repository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCategory(nonExistingId));
        verify(repository, never()).deleteById(any());
    }
}