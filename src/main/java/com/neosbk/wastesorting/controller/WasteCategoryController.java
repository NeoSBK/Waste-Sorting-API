package com.neosbk.wastesorting.controller;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.service.WasteCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waste/waste-category")
public class WasteCategoryController {

    @Autowired
    WasteCategoryService wasteCategoryService;

    // Read Category
    @GetMapping
    public ResponseEntity<List<WasteCategoryDTO>> getAllWasteCategories() {
        List<WasteCategoryDTO> categories = wasteCategoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    // Read Category
    @GetMapping("/{id}")
    public ResponseEntity<WasteCategoryDTO> getWasteCategoryById(@Valid @PathVariable Long id){
        WasteCategoryDTO category = wasteCategoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // Create Category
    @PostMapping
    public ResponseEntity<WasteCategoryDTO> createWasteCategory(@Valid @RequestBody WasteCategoryDTO wasteCategory){
        WasteCategoryDTO createCategory = wasteCategoryService.createCategory(wasteCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategory);
    }

    // Update Category
    @PutMapping("/{id}")
    ResponseEntity<WasteCategoryDTO> updateWasteCategory(@PathVariable Long id, @Valid @RequestBody WasteCategoryDTO wasteCategory){
        WasteCategoryDTO updateCategory = wasteCategoryService.updateCategory(id, wasteCategory);
        return ResponseEntity.ok(updateCategory);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteWasteCategory(@Valid @PathVariable Long id){
        wasteCategoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
