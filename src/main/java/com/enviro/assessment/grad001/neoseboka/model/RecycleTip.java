package com.enviro.assessment.grad001.neoseboka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class RecycleTip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    private String ItemName;

    @NotBlank(message = "Recycling tip description is required")
    private String recyclingTip;


    public RecycleTip() {}
    public RecycleTip(String ItemName, String recyclingTip) {
        this.ItemName = ItemName;
        this.recyclingTip = recyclingTip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        this.ItemName = itemName;
    }

    public String getRecyclingTip() {
        return recyclingTip;
    }

    public void setRecyclingTip(String recyclingTip) {
        this.recyclingTip = recyclingTip;
    }
}