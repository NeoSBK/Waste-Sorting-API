package com.neosbk.wastesorting.controller;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.model.DisposalGuideline;
import com.neosbk.wastesorting.service.DisposalGuidelineService;
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

@WebMvcTest(DisposalGuidelineController.class)
class DisposalGuidelineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DisposalGuidelineService disposalGuidelineService;

    private final static String BASE_URL = "/api/waste/disposal-guideline";
    private final DisposalGuideline guideline1 = new DisposalGuideline(1L, "Plastic Bottle", "Rinse and place in the recycling bin.");
    private final DisposalGuideline guideline2 = new DisposalGuideline(2L, "Glass Jar", "Rinse and place in the recycling bin.");


    @Test
    void shouldReturnGuideline() throws Exception {

        DisposalGuidelineDTO guidelineDTO = new DisposalGuidelineDTO(guideline1.getItemName(), guideline1.getDisposalGuideline());
        when(disposalGuidelineService.getGuidelineById(anyLong())).thenReturn(guidelineDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", guideline1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Plastic Bottle"))
                .andExpect(jsonPath("$.disposalGuideline").value("Rinse and place in the recycling bin."));
    }

    @Test
    void shouldReturnAllGuidelines() throws Exception {

        List<DisposalGuidelineDTO> guidelines = List.of(
                new DisposalGuidelineDTO(guideline1.getItemName(), guideline1.getDisposalGuideline()),
                new DisposalGuidelineDTO(guideline2.getItemName(), guideline2.getDisposalGuideline()));

        when(disposalGuidelineService.getDisposalGuidelines()).thenReturn(guidelines);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].itemName").value("Plastic Bottle"))
                .andExpect(jsonPath("$[0].disposalGuideline").value("Rinse and place in the recycling bin."))
                .andExpect(jsonPath("$[1].itemName").value("Glass Jar"))
                .andExpect(jsonPath("$[1].disposalGuideline").value("Rinse and place in the recycling bin."));

        verify(disposalGuidelineService).getDisposalGuidelines();
    }

    @Test
    void shouldCreatedAGuideline() throws Exception {

        DisposalGuidelineDTO disposalGuidelineDTO = new DisposalGuidelineDTO(guideline1.getItemName(), guideline1.getDisposalGuideline());
        when(disposalGuidelineService.createDisposalGuideline(any(DisposalGuidelineDTO.class))).thenReturn(disposalGuidelineDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guideline1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Plastic Bottle"))
                .andExpect(jsonPath("$.disposalGuideline").value("Rinse and place in the recycling bin."));

        verify(disposalGuidelineService).createDisposalGuideline(any(DisposalGuidelineDTO.class));
    }

    @Test
    void shouldUpdateAGuideline() throws Exception {
        Long guidelineId = guideline1.getId();
        DisposalGuidelineDTO updatedGuideline = new DisposalGuidelineDTO("Updated Item", "New Guideline");
        when(disposalGuidelineService.updateDisposalGuideline(eq(guidelineId), any(DisposalGuidelineDTO.class)))
                .thenReturn(updatedGuideline);

        mockMvc.perform(put(BASE_URL + "/{id}", guidelineId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGuideline)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Updated Item"))
                .andExpect(jsonPath("$.disposalGuideline").value("New Guideline"));

        verify(disposalGuidelineService).updateDisposalGuideline(eq(guidelineId),
                argThat(dto ->
                        dto.itemName().equals("Updated Item") &&
                                dto.disposalGuideline().equals("New Guideline")));
    }

    @Test
    void shouldReturnNoContent() throws Exception {

        Long guidelineId = guideline1.getId();
        doNothing().when(disposalGuidelineService).deleteDisposalGuideline(guidelineId);

        mockMvc.perform(delete(BASE_URL + "/{id}", guidelineId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(disposalGuidelineService).deleteDisposalGuideline(guidelineId);
    }
}