package com.neosbk.wastesorting.repository;

import com.neosbk.wastesorting.model.WasteCategory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WasteCategoryRepository extends JpaRepository<WasteCategory, Long> {}
