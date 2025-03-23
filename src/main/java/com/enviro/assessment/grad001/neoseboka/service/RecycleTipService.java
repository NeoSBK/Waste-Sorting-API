package com.enviro.assessment.grad001.neoseboka.service;

import com.enviro.assessment.grad001.neoseboka.exception.ResourceNotFoundException;
import com.enviro.assessment.grad001.neoseboka.model.RecycleTip;
import com.enviro.assessment.grad001.neoseboka.repository.RecycleTipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecycleTipService {

    @Autowired
    private RecycleTipRepository recycleTipRepository;

    public List<RecycleTip> getRecycleTips() {
        return recycleTipRepository.findAll();
    }

    public RecycleTip getRecycleTipById(Long id){
        return recycleTipRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recycle Tip Not Found For ID: " + id));
    }

    public RecycleTip createRecycleTip(RecycleTip recycleTip){
        return recycleTipRepository.save(recycleTip);
    }

    public RecycleTip updateRecycleTip(Long id, RecycleTip recycleTip){
       RecycleTip updateRecycleTip = this.getRecycleTipById(id);
       updateRecycleTip.setRecyclingTip(recycleTip.getRecyclingTip());
       updateRecycleTip.setItemName(recycleTip.getItemName());
       return recycleTipRepository.save(updateRecycleTip);
    }

    public void deleteRecycleTip(Long id){
        if(!recycleTipRepository.existsById(id)){
            throw new ResourceNotFoundException("Recycle Tip Not Found For ID: " + id);
        }
        recycleTipRepository.deleteById(id);
    }

}
