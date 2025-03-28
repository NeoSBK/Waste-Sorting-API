package com.neosbk.wastesorting.controller;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.model.RecycleTip;
import com.neosbk.wastesorting.service.RecycleTipService;
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

@WebMvcTest(RecycleTipController.class)
public class RecycleTipTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecycleTipService recycleTipService;

    private static final String BASE_URL = "/api/waste/recycle-tip";
    private final RecycleTip tip = new RecycleTip(1L, "Plastic Bottle", "Remove the cap before recycling.");
    private final RecycleTip tip2 = new RecycleTip(2L, "Newspaper", "Do not recycle wet or soiled paper.");

    @Test
    void shouldReturnATip() throws Exception {

        RecycleTipDTO recycleTipDTO = new RecycleTipDTO(tip.getItemName(), tip.getRecyclingTip());
        when(recycleTipService.getRecycleTipById(anyLong())).thenReturn(recycleTipDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", tip.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value(recycleTipDTO.itemName()))
                .andExpect(jsonPath("$.recyclingTip").value(recycleTipDTO.recyclingTip()));
    }

    @Test
    void shouldReturnTips() throws Exception {

        List<RecycleTipDTO> tips = List.of(
                        new RecycleTipDTO(tip.getItemName(), tip.getRecyclingTip()),
                        new RecycleTipDTO(tip2.getItemName(), tip2.getRecyclingTip()));
        
        when(recycleTipService.getRecycleTips()).thenReturn(tips);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(tips.size()))
                .andExpect(jsonPath("$[0].itemName").value("Plastic Bottle"))
                .andExpect(jsonPath("$[0].recyclingTip").value("Remove the cap before recycling."))
                .andExpect(jsonPath("$[1].itemName").value("Newspaper"))
                .andExpect(jsonPath("$[1].recyclingTip").value("Do not recycle wet or soiled paper."));

        verify(recycleTipService).getRecycleTips();
    }

    @Test
    void shouldCreateATip() throws Exception {

        RecycleTipDTO tipDTO = new RecycleTipDTO(tip.getItemName(), tip.getRecyclingTip());
        when(recycleTipService.createRecycleTip(any(RecycleTipDTO.class))).thenReturn(tipDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tip)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Plastic Bottle"))
                .andExpect(jsonPath("$.recyclingTip").value("Remove the cap before recycling."));

        verify(recycleTipService).createRecycleTip(any(RecycleTipDTO.class));
    }

    @Test
    void shouldUpdateATip() throws Exception {
        Long tipId = tip.getId();
        RecycleTipDTO updatedTip = new RecycleTipDTO( "Updated Item", "Updated Item Description");
        when(recycleTipService.updateRecycleTip(eq(tipId), any(RecycleTipDTO.class))).thenReturn(updatedTip);

        mockMvc.perform(put(BASE_URL + "/{id}", tipId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTip)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value(updatedTip.itemName()))
                .andExpect(jsonPath("$.recyclingTip").value(updatedTip.recyclingTip()));

        verify(recycleTipService).updateRecycleTip(eq(tipId),
                argThat(dto ->
                        dto.itemName().equals("Updated Item") && dto.recyclingTip().equals("Updated Item Description")));
    }

    @Test
    void shouldDeleteATip() throws Exception {
        Long tipId = tip.getId();
        doNothing().when(recycleTipService).deleteRecycleTip(tipId);

        mockMvc.perform(delete(BASE_URL + "/{id}", tipId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(recycleTipService).deleteRecycleTip(tipId);
    }
}

