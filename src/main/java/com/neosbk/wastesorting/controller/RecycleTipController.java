package com.neosbk.wastesorting.controller;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.model.RecycleTip;
import com.neosbk.wastesorting.service.RecycleTipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waste/recycle-tip")
public class RecycleTipController {

    @Autowired
    private RecycleTipService recycleTipService;

    // Read all tips
    @GetMapping
    public ResponseEntity<List<RecycleTipDTO>> getRecycleTips() {
        List<RecycleTipDTO> recycleTips = recycleTipService.getRecycleTips();
        return ResponseEntity.ok(recycleTips);
    }

    // Read specific tip
    @GetMapping("/{id}")
    public ResponseEntity<RecycleTipDTO> getRecycleTip(@PathVariable Long id){
        RecycleTipDTO recycleTip = recycleTipService.getRecycleTipById(id);
        return ResponseEntity.ok(recycleTip);
    }

    // Create a tip
    @PostMapping
    public ResponseEntity<RecycleTipDTO> createRecycleTip(@Valid @RequestBody RecycleTipDTO recycleTip){
        RecycleTipDTO newRecycleTip = recycleTipService.createRecycleTip(recycleTip);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecycleTip);
    }

    // Update a tip
    @PutMapping("/{id}")
    public ResponseEntity<RecycleTipDTO> updateRecycleTip(@PathVariable Long id, @Valid @RequestBody RecycleTipDTO recycleTip){
        RecycleTipDTO updatedRecycleTip = recycleTipService.updateRecycleTip(id, recycleTip);
        return ResponseEntity.ok(updatedRecycleTip);
    }

    // Delete a tip
    @DeleteMapping("/{id}")
    public ResponseEntity<RecycleTip> deleteRecycleTip(@PathVariable Long id){
        recycleTipService.deleteRecycleTip(id);
        return ResponseEntity.noContent().build();
    }
}
