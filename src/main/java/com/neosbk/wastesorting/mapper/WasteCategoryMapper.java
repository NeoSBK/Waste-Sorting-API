package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.model.WasteCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WasteCategoryMapper {
    WasteCategoryDTO toDTO(WasteCategory wasteCategory);
    WasteCategory toEntity(WasteCategoryDTO wasteCategoryDTO);
}
