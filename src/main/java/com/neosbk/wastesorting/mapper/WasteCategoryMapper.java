package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.model.WasteCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WasteCategoryMapper {
    WasteCategoryMapper INSTANCE = Mappers.getMapper(WasteCategoryMapper.class);

    WasteCategoryDTO toDTO(WasteCategory wasteCategory);
    WasteCategory toEntity(WasteCategoryDTO wasteCategoryDTO);
}
