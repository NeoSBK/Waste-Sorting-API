package com.neosbk.wastesorting.controller;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.model.WasteCategory;
import com.neosbk.wastesorting.service.WasteCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WasteCategoryController.class)
public class WasteCategoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WasteCategoryService wasteCategoryService;

    private static final String BASE_URL = "/api/waste/waste-category";
    private final WasteCategory category = new WasteCategory(1L, "Plastic Bottle", "Rinse and recycle");
    private final WasteCategory category2 = new WasteCategory(2L, "Glass", "Wash and separate by color");

    @Test
    void shouldReturnACategory() throws Exception {

        WasteCategoryDTO categoryDTO = new WasteCategoryDTO(category.getCategoryName(), category.getCategoryDescription());
        when(wasteCategoryService.getCategoryById(anyLong())).thenReturn(categoryDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value(categoryDTO.categoryName()))
                .andExpect(jsonPath("$.categoryDescription").value(categoryDTO.categoryDescription()));
    }

    @Test
    void shouldReturnAllCategories() throws Exception {

        List<WasteCategoryDTO> categories = List.of(
                new WasteCategoryDTO(category.getCategoryName(), category.getCategoryDescription()),
                new WasteCategoryDTO(category2.getCategoryName(), category2.getCategoryDescription()));

        when(wasteCategoryService.getCategories()).thenReturn(categories);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(categories.size()))
                .andExpect(jsonPath("$[0].categoryName").value("Plastic Bottle"))
                .andExpect(jsonPath("$[0].categoryDescription").value("Rinse and recycle"))
                .andExpect(jsonPath("$[1].categoryName").value("Glass"))
                .andExpect(jsonPath("$[1].categoryDescription").value("Wash and separate by color"));

        verify(wasteCategoryService).getCategories();
    }

    @Test
    void shouldCreateCategory() throws Exception {

        WasteCategoryDTO CategoryDTO = new WasteCategoryDTO(category.getCategoryName(), category.getCategoryDescription());
        when(wasteCategoryService.createCategory(any(WasteCategoryDTO.class))).thenReturn(CategoryDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CategoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Plastic Bottle"))
                .andExpect(jsonPath("$.categoryDescription").value("Rinse and recycle"));

        verify(wasteCategoryService).createCategory(any(WasteCategoryDTO.class));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Long categoryId = category2.getId();
        WasteCategoryDTO updateRequest = new WasteCategoryDTO("Updated Category","New instructions");
        when(wasteCategoryService.updateCategory(eq(categoryId), any(WasteCategoryDTO.class))).thenReturn(updateRequest);

        mockMvc.perform(put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Updated Category"))
                .andExpect(jsonPath("$.categoryDescription").value("New instructions"));

        verify(wasteCategoryService).updateCategory(eq(categoryId),
                argThat(dto ->
                        dto.categoryName().equals("Updated Category") &&
                                dto.categoryDescription().equals("New instructions")
                )
        )   ;
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        Long categoryId = category.getId();
        doNothing().when(wasteCategoryService).deleteCategory(categoryId);

        mockMvc.perform(delete(BASE_URL + "/{id}", categoryId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(wasteCategoryService, times(1)).deleteCategory(categoryId);
    }
}
