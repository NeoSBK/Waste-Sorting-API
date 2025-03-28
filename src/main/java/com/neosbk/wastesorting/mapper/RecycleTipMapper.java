package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.model.RecycleTip;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecycleTipMapper {
    RecycleTipMapper INSTANCE = Mappers.getMapper(RecycleTipMapper.class);

    RecycleTipDTO toDTO(RecycleTip RecycleTip);
    RecycleTip toEntity(RecycleTipDTO RecycleTipDTO);
}
