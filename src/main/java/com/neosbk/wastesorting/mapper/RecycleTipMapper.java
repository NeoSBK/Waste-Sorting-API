package com.neosbk.wastesorting.mapper;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.model.RecycleTip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecycleTipMapper {
    RecycleTipDTO toDTO(RecycleTip RecycleTip);
    RecycleTip toEntity(RecycleTipDTO RecycleTipDTO);
}
