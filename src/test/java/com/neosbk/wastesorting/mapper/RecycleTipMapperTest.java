package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.model.RecycleTip;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RecycleTipMapperTest {

    private final RecycleTipMapper mapper = Mappers.getMapper(RecycleTipMapper.class);

    @Test
    void shouldMapRecycleTipEntityToRecycleTipDTO() {
        RecycleTip recycleTip = new RecycleTip(1L,"Newspaper","Do not recycle wet or soiled paper.");

        RecycleTipDTO result = mapper.toDTO(recycleTip);

        assertThat(result).isNotNull();
        assertThat(result.itemName()).isEqualTo("Newspaper");
        assertThat(result.recyclingTip()).isEqualTo("Do not recycle wet or soiled paper.");
    }

    @Test
    void shouldMapRecycleTipDTOToRecycleTipEntity() {
        RecycleTipDTO recycleTipDTO = new RecycleTipDTO("Newspaper","Do not recycle wet or soiled paper.");

        RecycleTip result = mapper.toEntity(recycleTipDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getItemName()).isEqualTo("Newspaper");
        assertThat(result.getRecyclingTip()).isEqualTo("Do not recycle wet or soiled paper.");
    }

    @Test
    void shouldHandleNullRecycleTips() {
        assertThat(mapper.toEntity(null)).isNull();
        assertThat(mapper.toDTO(null)).isNull();
    }
}
