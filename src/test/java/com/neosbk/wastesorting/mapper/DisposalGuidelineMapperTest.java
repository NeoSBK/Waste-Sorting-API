package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.model.DisposalGuideline;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.assertj.core.api.Assertions.assertThat;

public class DisposalGuidelineMapperTest {

    private final DisposalGuidelineMapper mapper = Mappers.getMapper(DisposalGuidelineMapper.class);

    @Test
    void shouldMapDisposalGuidelineEntityToDisposalGuidelineDto(){
        DisposalGuideline guideline = new DisposalGuideline(5L,"Plastic Bottle","Rinse and place in the recycling bin.");

        DisposalGuidelineDTO result = mapper.toDTO(guideline);

        assertThat(result).isNotNull();
        assertThat(result.itemName()).isEqualTo("Plastic Bottle");
        assertThat(result.disposalGuideline()).isEqualTo("Rinse and place in the recycling bin.")   ;
    }

    @Test
    void shouldMapDisposalGuidelineDtoToDisposalGuidelineEntity(){
        DisposalGuidelineDTO guidelineDTO = new DisposalGuidelineDTO("Plastic Bottle","Rinse and place in the recycling bin.");

        DisposalGuideline result = mapper.toEntity(guidelineDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getItemName()).isEqualTo("Plastic Bottle");
        assertThat(result.getDisposalGuideline()).isEqualTo("Rinse and place in the recycling bin.");
    }

    @Test
    void shouldHandleNullDisposalGuidelines() {
        assertThat(mapper.toDTO(null)).isNull();
        assertThat(mapper.toEntity(null)).isNull();
    }
}
