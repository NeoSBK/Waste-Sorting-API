package com.neosbk.wastesorting.service;

import com.neosbk.wastesorting.dto.DisposalGuidelineDTO;
import com.neosbk.wastesorting.exception.ResourceNotFoundException;
import com.neosbk.wastesorting.mapper.DisposalGuidelineMapper;
import com.neosbk.wastesorting.model.DisposalGuideline;

import com.neosbk.wastesorting.repository.DisposalGuidelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisposalGuidelineService {

    @Autowired
    private final DisposalGuidelineRepository disposalGuidelineRepository;
    private final DisposalGuidelineMapper disposalGuidelineMapper;

    public DisposalGuidelineService(DisposalGuidelineRepository disposalGuidelineRepository, DisposalGuidelineMapper disposalGuidelineMapper) {
        this.disposalGuidelineRepository = disposalGuidelineRepository;
        this.disposalGuidelineMapper = disposalGuidelineMapper;
    }

    @Transactional(readOnly = true)
    public List<DisposalGuidelineDTO> getDisposalGuidelines() {
        return disposalGuidelineRepository.findAll()
                .stream()
                .map(disposalGuidelineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DisposalGuidelineDTO getGuidelineById(Long id) {
        return disposalGuidelineRepository.findById(id)
                .map(disposalGuidelineMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Disposal Guideline Not Found For ID: " + id));
    }

    @Transactional
    public DisposalGuidelineDTO createDisposalGuideline(DisposalGuidelineDTO guideline){
        DisposalGuideline disposalGuideline = disposalGuidelineMapper.toEntity(guideline);
        DisposalGuideline newGuideline = disposalGuidelineRepository.save(disposalGuideline);
        return disposalGuidelineMapper.toDTO(newGuideline);
    }

    @Transactional
    public DisposalGuidelineDTO updateDisposalGuideline(Long id, DisposalGuidelineDTO guidelineDTO){
        DisposalGuideline existingGuideline = disposalGuidelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disposal Guideline Not Found For ID: " + id));

        if(guidelineDTO.itemName() != null){
            existingGuideline.setItemName(guidelineDTO.itemName());
        }
        if(guidelineDTO.disposalGuideline() != null){
            existingGuideline.setDisposalGuideline(guidelineDTO.disposalGuideline());
        }

        DisposalGuideline updatedGuideline =  disposalGuidelineRepository.save(existingGuideline);
        return disposalGuidelineMapper.toDTO(updatedGuideline);
    }

    @Transactional
    public void deleteDisposalGuideline(Long id){
        if(!disposalGuidelineRepository.existsById(id)){
            throw new ResourceNotFoundException("Disposal Guideline Not Found For ID: " + id);
        }
        disposalGuidelineRepository.deleteById(id);
    }
}
