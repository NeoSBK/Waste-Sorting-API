package com.enviro.assessment.grad001.neoseboka.controller;

import com.enviro.assessment.grad001.neoseboka.model.WasteCategory;
import com.enviro.assessment.grad001.neoseboka.service.WasteCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waste/waste-categories")
public class WasteCategoryController {

    @Autowired
    WasteCategoryService wasteCategoryService;

    // Read Category
    @GetMapping
    public ResponseEntity<List<WasteCategory>> getAllWasteCategories() {
        List<WasteCategory> categories = wasteCategoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    // Read Category
    @GetMapping("/{id}")
    public ResponseEntity<WasteCategory> getWasteCategoryById(@Valid @PathVariable Long id){
        WasteCategory category = wasteCategoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Create Category
    @PostMapping
    public ResponseEntity<WasteCategory> createWasteCategory(@Valid @RequestBody WasteCategory wasteCategory){
        WasteCategory createCategory = wasteCategoryService.createCategory(wasteCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategory);
    }

    // Update Category
    @PutMapping("/{id}")
    ResponseEntity<WasteCategory> updateWasteCategory(@Valid @PathVariable Long id, @Valid @RequestBody WasteCategory wasteCategory){
        WasteCategory updateCategory = wasteCategoryService.updateCategory(id, wasteCategory);
        return ResponseEntity.ok(updateCategory);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    ResponseEntity<WasteCategory> deleteWasteCategory(@Valid @PathVariable Long id){
        wasteCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
