package com.enviro.assessment.grad001.neoseboka.service;

import com.enviro.assessment.grad001.neoseboka.model.WasteCategory;
import com.enviro.assessment.grad001.neoseboka.repository.WasteCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.enviro.assessment.grad001.neoseboka.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WasteCategoryService {

    @Autowired
    private WasteCategoryRepository wasteCategoryRepository;

    public List<WasteCategory> getCategories() {
        return wasteCategoryRepository.findAll();
    }

    public WasteCategory getCategoryById(Long id) {
        return wasteCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category Not Found For ID: " + id));
    }

    public WasteCategory createCategory(WasteCategory category){
        return wasteCategoryRepository.save(category);
    }

    public WasteCategory updateCategory(Long id, WasteCategory category){
        WasteCategory updatedCategory = this.getCategoryById(id);
        updatedCategory.setCategoryName(category.getCategoryName());
        updatedCategory.setCategoryDescription(category.getCategoryDescription());
        return wasteCategoryRepository.save(updatedCategory);
    }

    public void deleteCategory(Long id) {
        if (!wasteCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        wasteCategoryRepository.deleteById(id);
    }
}
