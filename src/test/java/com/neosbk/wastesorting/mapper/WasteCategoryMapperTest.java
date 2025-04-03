package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.WasteCategoryDTO;
import com.neosbk.wastesorting.model.WasteCategory;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WasteCategoryMapperTest {

    private final WasteCategoryMapper mapper = Mappers.getMapper(WasteCategoryMapper.class);

    @Test
    void shouldMapWasteCategoryEntityToWasteCategoryDTO() {
        WasteCategory category = new WasteCategory(3L,"Glass","Recyclable glass materials");

        WasteCategoryDTO result = mapper.toDTO(category);

        assertThat(result).isNotNull();
        assertThat(result.categoryName()).isEqualTo("Glass");
        assertThat(result.categoryDescription()).isEqualTo("Recyclable glass materials");
    }

    @Test
    void shouldMapWasteCategoryDTOToWasteCategoryEntity() {
        WasteCategoryDTO categoryDTO = new WasteCategoryDTO("Glass","Recyclable glass materials");

        WasteCategory result = mapper.toEntity(categoryDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getCategoryName()).isEqualTo("Glass");
        assertThat(result.getCategoryDescription()).isEqualTo("Recyclable glass materials");
    }

    @Test
    void shouldHandleNullCategories() {
        assertThat(mapper.toEntity(null)).isNull();
        assertThat(mapper.toDTO(null)).isNull();
    }
}
