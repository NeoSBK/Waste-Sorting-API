package com.neosbk.wastesorting.service;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.mapper.WasteCategoryMapper;
import com.neosbk.wastesorting.model.WasteCategory;
import com.neosbk.wastesorting.repository.WasteCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.neosbk.wastesorting.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WasteCategoryService {

    @Autowired
    private final WasteCategoryRepository wasteCategoryRepository;
    private final WasteCategoryMapper wasteCategoryMapper;

    public WasteCategoryService(WasteCategoryRepository wasteCategoryRepository, WasteCategoryMapper wasteCategoryMapper) {
        this.wasteCategoryRepository = wasteCategoryRepository;
        this.wasteCategoryMapper = wasteCategoryMapper;
    }

    @Transactional(readOnly = true)
    public List<WasteCategoryDTO> getCategories() {
        return wasteCategoryRepository.findAll()
                .stream()
                .map(wasteCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WasteCategoryDTO getCategoryById(Long id) {
        return wasteCategoryRepository.findById(id)
                .map(wasteCategoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found For ID: " + id));
    }

    @Transactional
    public WasteCategoryDTO createCategory(WasteCategoryDTO categoryDTO){
        WasteCategory category = wasteCategoryMapper.toEntity(categoryDTO);
        WasteCategory newCategory = wasteCategoryRepository.save(category);
        return wasteCategoryMapper.toDTO(newCategory);
    }

    @Transactional
    public WasteCategoryDTO updateCategory(Long id, WasteCategoryDTO categoryDTO){
        WasteCategory existingCategory = wasteCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found For ID: " + id));

        if(categoryDTO.categoryName() != null){
            existingCategory.setCategoryName(categoryDTO.categoryName());
        }
        if(categoryDTO.categoryDescription() != null) {
            existingCategory.setCategoryDescription(categoryDTO.categoryDescription());
        }

        WasteCategory updatedCategory = wasteCategoryRepository.save(existingCategory);
        return wasteCategoryMapper.toDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!wasteCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        wasteCategoryRepository.deleteById(id);
    }
}
