package com.enviro.assessment.grad001.neoseboka.controller;

import com.enviro.assessment.grad001.neoseboka.model.DisposalGuideline;
import com.enviro.assessment.grad001.neoseboka.service.DisposalGuidelineService;
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

    // Read all guidelines
    @GetMapping
    public ResponseEntity<List<DisposalGuideline>> getGuidelines(){
        List<DisposalGuideline> guidelines = disposalGuidelineService.getDisposalGuidelines();
        return ResponseEntity.ok(guidelines);
    }

    // Read specific guideline
    @GetMapping("/{id}")
    public ResponseEntity<DisposalGuideline> getGuidelineById(@PathVariable Long id){
        DisposalGuideline guideline = disposalGuidelineService.getGuidelineById(id);
        return ResponseEntity.ok(guideline);
    }

    // Create new guideline
    @PostMapping
    public ResponseEntity<DisposalGuideline> createGuideline(@Valid @RequestBody DisposalGuideline guideline){
        DisposalGuideline newGuideline = disposalGuidelineService.createDisposalGuideline(guideline);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGuideline);
    }

    // Update existing guideline
    @PutMapping("/{id}")
    public ResponseEntity<DisposalGuideline> updateGuideline(@PathVariable Long id, @Valid @RequestBody DisposalGuideline guideline){
        DisposalGuideline updatedGuideline = disposalGuidelineService.updateDisposalGuideline(id, guideline);
        return ResponseEntity.ok(updatedGuideline);
    }

    // Delete a guideline
    @DeleteMapping("/{id}")
    public ResponseEntity<DisposalGuideline> deleteGuideline(@PathVariable Long id){
        disposalGuidelineService.deleteDisposalGuideline(id);
        return ResponseEntity.noContent().build();
    }
}
