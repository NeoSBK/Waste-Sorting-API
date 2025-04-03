package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.model.DisposalGuideline;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DisposalGuidelineMapper {
    DisposalGuidelineDTO toDTO(DisposalGuideline disposalGuideline);
    DisposalGuideline toEntity(DisposalGuidelineDTO disposalGuidelineDTO);
}
