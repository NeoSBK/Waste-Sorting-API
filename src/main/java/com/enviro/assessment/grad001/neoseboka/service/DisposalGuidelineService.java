package com.enviro.assessment.grad001.neoseboka.service;

import com.enviro.assessment.grad001.neoseboka.exception.ResourceNotFoundException;
import com.enviro.assessment.grad001.neoseboka.model.DisposalGuideline;

import com.enviro.assessment.grad001.neoseboka.repository.DisposalGuidelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DisposalGuidelineService {

    @Autowired
    private DisposalGuidelineRepository disposalGuidelineRepository;

    public List<DisposalGuideline> getDisposalGuidelines() {
        return disposalGuidelineRepository.findAll();
    }

    public DisposalGuideline getGuidelineById(Long id) {
        return disposalGuidelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disposal Guideline Not Found For ID: " + id));
    }

    public DisposalGuideline createDisposalGuideline(DisposalGuideline guideline){
        return disposalGuidelineRepository.save(guideline);
    }

    public DisposalGuideline updateDisposalGuideline(Long id, DisposalGuideline guideline){
        DisposalGuideline updatedDisposalGuideline = this.getGuidelineById(id);
        updatedDisposalGuideline.setItemName(guideline.getItemName());
        updatedDisposalGuideline.setDisposalGuideline(guideline.getDisposalGuideline());
        return disposalGuidelineRepository.save(updatedDisposalGuideline);
    }

    public void deleteDisposalGuideline(Long id){
        if(!disposalGuidelineRepository.existsById(id)){
            throw new ResourceNotFoundException("Disposal Guideline Not Found For ID: " + id);
        }
        disposalGuidelineRepository.deleteById(id);
    }
}
