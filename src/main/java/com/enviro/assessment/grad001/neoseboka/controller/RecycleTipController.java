package com.enviro.assessment.grad001.neoseboka.controller;

import com.enviro.assessment.grad001.neoseboka.model.RecycleTip;
import com.enviro.assessment.grad001.neoseboka.service.RecycleTipService;
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
    public ResponseEntity<List<RecycleTip>> getRecycleTips() {
        List<RecycleTip> recycleTips = recycleTipService.getRecycleTips();
        return ResponseEntity.ok(recycleTips);
    }

    // Read specific tip
    @GetMapping("/{id}")
    public ResponseEntity<RecycleTip> getRecycleTip(@PathVariable Long id){
        RecycleTip recycleTip = recycleTipService.getRecycleTipById(id);
        return ResponseEntity.ok(recycleTip);
    }

    // Create a tip
    @PostMapping
    public ResponseEntity<RecycleTip> createRecycleTip(@Valid @RequestBody RecycleTip recycleTip){
        RecycleTip newRecycleTip = recycleTipService.createRecycleTip(recycleTip);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRecycleTip);
    }

    // Update a tip
    @PutMapping("/{id}")
    public ResponseEntity<RecycleTip> updateRecycleTip(@PathVariable Long id, @Valid @RequestBody RecycleTip recycleTip){
        RecycleTip updatedRecycleTip = recycleTipService.updateRecycleTip(id, recycleTip);
        return ResponseEntity.ok(updatedRecycleTip);
    }

    // Delete a tip
    @DeleteMapping("/{id}")
    public ResponseEntity<RecycleTip> deleteRecycleTip(@PathVariable Long id){
        recycleTipService.deleteRecycleTip(id);
        return ResponseEntity.noContent().build();
    }
}
