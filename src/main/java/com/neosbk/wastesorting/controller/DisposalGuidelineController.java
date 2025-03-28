package com.neosbk.wastesorting.controller;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.model.DisposalGuideline;
import com.neosbk.wastesorting.service.DisposalGuidelineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waste/disposal-guideline")
public class DisposalGuidelineController {

    @Autowired
    private DisposalGuidelineService disposalGuidelineService;


    // Read specific guideline
    @GetMapping("/{id}")
    public ResponseEntity<DisposalGuidelineDTO> getGuidelineById(@PathVariable Long id){
        DisposalGuidelineDTO guideline = disposalGuidelineService.getGuidelineById(id);
        return ResponseEntity.ok(guideline);
    }

    // Read all guidelines
    @GetMapping
    public ResponseEntity<List<DisposalGuidelineDTO>> getGuidelines(){
        List<DisposalGuidelineDTO> guidelines = disposalGuidelineService.getDisposalGuidelines();
        return ResponseEntity.ok(guidelines);
    }

    // Create new guideline
    @PostMapping
    public ResponseEntity<DisposalGuidelineDTO> createGuideline(@Valid @RequestBody DisposalGuidelineDTO guideline){
        DisposalGuidelineDTO newGuideline = disposalGuidelineService.createDisposalGuideline(guideline);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGuideline);
    }

    // Update existing guideline
    @PutMapping("/{id}")
    public ResponseEntity<DisposalGuidelineDTO> updateGuideline(@PathVariable Long id, @Valid @RequestBody DisposalGuidelineDTO guideline){
        DisposalGuidelineDTO updatedGuideline = disposalGuidelineService.updateDisposalGuideline(id, guideline);
        return ResponseEntity.ok(updatedGuideline);
    }

    // Delete a guideline
    @DeleteMapping("/{id}")
    public ResponseEntity<DisposalGuideline> deleteGuideline(@PathVariable Long id){
        disposalGuidelineService.deleteDisposalGuideline(id);
        return ResponseEntity.noContent().build();
    }
}
