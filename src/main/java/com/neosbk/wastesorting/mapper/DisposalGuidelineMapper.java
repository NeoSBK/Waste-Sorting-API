package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.model.DisposalGuideline;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DisposalGuidelineMapper {
    DisposalGuidelineMapper INSTANCE = Mappers.getMapper(DisposalGuidelineMapper.class);

    DisposalGuidelineDTO toDTO(DisposalGuideline disposalGuideline);
    DisposalGuideline toEntity(DisposalGuidelineDTO disposalGuidelineDTO);
}
