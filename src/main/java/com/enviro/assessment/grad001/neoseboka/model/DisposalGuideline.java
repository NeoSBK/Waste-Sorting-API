package com.enviro.assessment.grad001.neoseboka.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class DisposalGuideline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Guideline description is required")
    private String disposalGuideline;


    public DisposalGuideline() {}
    public DisposalGuideline(String itemName, String disposalGuideline) {
        this.itemName = itemName;
        this.disposalGuideline = disposalGuideline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDisposalGuideline() {
        return disposalGuideline;
    }

    public void setDisposalGuideline(String disposalGuideline) {
        this.disposalGuideline = disposalGuideline;
    }

}