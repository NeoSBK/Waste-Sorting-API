package com.neosbk.wastesorting.service;

import com.neosbk.wastesorting.dto.RecycleTipDTO;
import com.neosbk.wastesorting.exception.ResourceNotFoundException;
import com.neosbk.wastesorting.mapper.RecycleTipMapper;
import com.neosbk.wastesorting.model.RecycleTip;
import com.neosbk.wastesorting.repository.RecycleTipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecycleTipService {

    @Autowired
    private final RecycleTipRepository recycleTipRepository;

    private final RecycleTipMapper recycleTipMapper;

    public RecycleTipService(RecycleTipRepository recycleTipRepository, RecycleTipMapper recycleTipMapper) {
        this.recycleTipRepository = recycleTipRepository;
        this.recycleTipMapper = recycleTipMapper;
    }

    @Transactional(readOnly = true)
    public List<RecycleTipDTO> getRecycleTips() {
        return recycleTipRepository.findAll()
                .stream()
                .map(recycleTipMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecycleTipDTO getRecycleTipById(Long id){
        return recycleTipRepository.findById(id)
                .map(recycleTipMapper:: toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Recycle Tip Not Found For ID: " + id));
    }

    @Transactional
    public RecycleTipDTO createRecycleTip(RecycleTipDTO recycleTipDTO){
        RecycleTip recycleTip = recycleTipMapper.toEntity(recycleTipDTO);
        RecycleTip newTip = recycleTipRepository.save(recycleTip);
        return recycleTipMapper.toDTO(newTip);
    }

    @Transactional
    public RecycleTipDTO updateRecycleTip(Long id, RecycleTipDTO recycleTipDTO){
       RecycleTip existingRecycleTip = recycleTipRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Recycle Tip Not Found For ID: " + id));

       if(recycleTipDTO.itemName() != null){
           existingRecycleTip.setItemName(recycleTipDTO.itemName());
       }
       if(recycleTipDTO.recyclingTip() != null){
           existingRecycleTip.setRecyclingTip(recycleTipDTO.recyclingTip());
       }

       RecycleTip updateRecycleTip = recycleTipRepository.save(existingRecycleTip);
       return recycleTipMapper.toDTO(updateRecycleTip);
    }

    public void deleteRecycleTip(Long id){
        if(!recycleTipRepository.existsById(id)){
            throw new ResourceNotFoundException("Recycle Tip Not Found For ID: " + id);
        }
        recycleTipRepository.deleteById(id);
    }

}
